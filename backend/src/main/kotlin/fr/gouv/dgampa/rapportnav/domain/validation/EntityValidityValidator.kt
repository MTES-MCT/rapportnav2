package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsErrorEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.springframework.stereotype.Service

/**
 * Spring service for validating entities.
 *
 * Two validation paths:
 * - **Jakarta** (via [validate] / [validateAndThrow]): structural constraints like @EndAfterStart, @WithinMissionDateRange.
 *   Used at save time with [ValidateThrowsBeforeSave] group.
 * - **Policy-based** (via [validateCompleteness] / [validateCompletenessWithSource]): required-field rules from [ValidationPolicy].
 *   Used for completeness computation.
 */
@Service
class EntityValidityValidator(
    private val validator: Validator
) {

    // =========================================================================
    // Jakarta-based validation (structural constraints, save-time)
    // =========================================================================

    /**
     * Validates entity using Jakarta Bean Validation with specified groups.
     * Used for structural constraints (@EndAfterStart, @WithinMissionDateRange).
     */
    fun validate(entity: Any, vararg groups: Class<*>): CompletenessForStatsEntity {
        val violations = validator.validate(entity, *groups).toSet()

        if (violations.isEmpty()) {
            return CompletenessForStatsEntity(
                status = CompletenessForStatsStatusEnum.VALID,
                errors = emptyList()
            )
        }

        val errors = violations.map { violation ->
            CompletenessForStatsErrorEntity(
                field = violation.propertyPath.toString().ifEmpty { "_class" },
                rule = violation.constraintDescriptor.annotation.annotationClass.simpleName ?: "unknown",
                message = violation.message
            )
        }

        return CompletenessForStatsEntity(
            status = CompletenessForStatsStatusEnum.INVALID,
            errors = errors
        )
    }

    /**
     * Validates entity and throws BackendUsageException if validation fails.
     * Use this in use cases to block saves with invalid structural data.
     */
    fun validateAndThrow(entity: Any, vararg groups: Class<*>) {
        val result = validate(entity, *groups)
        if (!result.isComplete) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Erreur de validation",
                data = mapOf("fieldErrors" to result.errors.map { error ->
                    mapOf(
                        "field" to error.field,
                        "message" to error.message,
                        "rule" to error.rule
                    )
                })
            )
        }
    }

    // =========================================================================
    // Policy-based validation (required fields, completeness)
    // =========================================================================

    /**
     * Validates entity completeness against a [ValidationPolicy]'s required-field rules.
     */
    fun validateCompleteness(entity: Any, policy: ValidationPolicy): CompletenessForStatsEntity {
        val violations = RequiredFieldsValidator.validate(entity, policy.rules)

        if (violations.isEmpty()) {
            return CompletenessForStatsEntity(
                status = CompletenessForStatsStatusEnum.VALID,
                errors = emptyList()
            )
        }

        val errors = violations.map { violation ->
            CompletenessForStatsErrorEntity(
                field = violation.field,
                rule = "RequiredFields",
                message = violation.message
            )
        }

        return CompletenessForStatsEntity(
            status = CompletenessForStatsStatusEnum.INCOMPLETE,
            errors = errors
        )
    }

    /**
     * Validates entity completeness and attributes errors to a specific source.
     * Used for multi-source actions (Env/Fish) where RAPPORT_NAV fields are validated separately.
     */
    fun validateCompletenessWithSource(
        entity: Any,
        source: MissionSourceEnum,
        policy: ValidationPolicy
    ): CompletenessForStatsEntity {
        val result = validateCompleteness(entity, policy)
        return if (result.status != CompletenessForStatsStatusEnum.VALID) {
            result.copy(sources = listOf(source))
        } else {
            result
        }
    }

    companion object {
        /**
         * Creates a default EntityValidityValidator for non-Spring contexts (tests, entity methods).
         * Note: Validators that require Spring DI (e.g., WithinMissionDateRangeValidator)
         * will gracefully degrade (skip validation) when used through this factory.
         */
        fun createDefault(): EntityValidityValidator {
            val factory = Validation.buildDefaultValidatorFactory()
            return EntityValidityValidator(factory.validator)
        }

        /**
         * Merges multiple completeness results (for multi-source actions like Env/Fish).
         */
        fun merge(vararg results: CompletenessForStatsEntity): CompletenessForStatsEntity {
            val allErrors = results.flatMap { it.errors }
            val allSources = results.mapNotNull { it.sources }.flatten().distinct()

            val worstStatus = when {
                results.any { it.status == CompletenessForStatsStatusEnum.INCOMPLETE } -> CompletenessForStatsStatusEnum.INCOMPLETE
                results.any { it.status == CompletenessForStatsStatusEnum.INVALID } -> CompletenessForStatsStatusEnum.INVALID
                else -> CompletenessForStatsStatusEnum.VALID
            }

            return CompletenessForStatsEntity(
                status = worstStatus,
                errors = allErrors,
                sources = allSources.ifEmpty { null }
            )
        }
    }
}
