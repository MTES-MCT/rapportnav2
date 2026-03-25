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
 * Spring service wrapper around Jakarta Bean Validation for validating entities.
 * Uses Spring-managed validators which can auto-fetch mission dates.
 */
@Service
class EntityValidityValidator(
    private val validator: Validator
) {
    /**
     * Validates entity using Jakarta Bean Validation with specified groups.
     *
     * @param entity The entity to validate
     * @param groups Validation groups to run (e.g., ValidateThrowsBeforeSave, ValidateWhenMissionFinished)
     * @return CompletenessForStatsEntity with status and any errors
     */
    fun validate(entity: Any, vararg groups: Class<*>): CompletenessForStatsEntity {
        val violations = validator.validate(entity, *groups)

        val errors = violations.map { violation ->
            CompletenessForStatsErrorEntity(
                field = violation.propertyPath.toString().ifEmpty { "_class" },
                rule = violation.constraintDescriptor.annotation.annotationClass.simpleName ?: "unknown",
                message = violation.message
            )
        }

        return CompletenessForStatsEntity(
            status = if (errors.isEmpty()) CompletenessForStatsStatusEnum.COMPLETE
            else CompletenessForStatsStatusEnum.INCOMPLETE,
            errors = errors
        )
    }

    /**
     * Validates entity and throws BackendUsageException if validation fails.
     * Use this in use cases to avoid repetitive validation boilerplate.
     *
     * @param entity The entity to validate
     * @param groups Validation groups to run
     * @throws BackendUsageException if validation fails
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

    /**
     * Validates entity and includes source information for multi-source actions.
     *
     * @param entity The entity to validate
     * @param source The source to attribute errors to (e.g., RAPPORT_NAV)
     * @param groups Validation groups to run
     * @return CompletenessForStatsEntity with status, errors, and source attribution
     */
    fun validateWithSource(
        entity: Any,
        source: MissionSourceEnum,
        vararg groups: Class<*>
    ): CompletenessForStatsEntity {
        val result = validate(entity, *groups)
        return if (result.status == CompletenessForStatsStatusEnum.INCOMPLETE) {
            result.copy(sources = listOf(source))
        } else {
            result
        }
    }

    companion object {
        // Fallback validator for non-Spring contexts (tests, etc.)
        private val fallbackValidator: Validator = Validation.buildDefaultValidatorFactory().validator

        /**
         * Merges multiple completeness results (for multi-source actions like Env/Fish).
         *
         * @param results List of completeness results to merge
         * @return Combined CompletenessForStatsEntity
         */
        fun merge(vararg results: CompletenessForStatsEntity): CompletenessForStatsEntity {
            val allErrors = results.flatMap { it.errors }
            val allSources = results.mapNotNull { it.sources }.flatten().distinct()

            return CompletenessForStatsEntity(
                status = if (allErrors.isEmpty()) CompletenessForStatsStatusEnum.COMPLETE
                else CompletenessForStatsStatusEnum.INCOMPLETE,
                errors = allErrors,
                sources = allSources.ifEmpty { null }
            )
        }

        /**
         * Static validation method for non-Spring contexts.
         * Note: This won't have access to auto-fetch mission dates.
         */
        fun validateStatic(entity: Any, vararg groups: Class<*>): CompletenessForStatsEntity {
            val violations = fallbackValidator.validate(entity, *groups)

            val errors = violations.map { violation ->
                CompletenessForStatsErrorEntity(
                    field = violation.propertyPath.toString().ifEmpty { "_class" },
                    rule = violation.constraintDescriptor.annotation.annotationClass.simpleName ?: "unknown",
                    message = violation.message
                )
            }

            return CompletenessForStatsEntity(
                status = if (errors.isEmpty()) CompletenessForStatsStatusEnum.COMPLETE
                else CompletenessForStatsStatusEnum.INCOMPLETE,
                errors = errors
            )
        }

        /**
         * Static validation with source for non-Spring contexts.
         * Note: This won't have access to auto-fetch mission dates.
         */
        fun validateWithSourceStatic(
            entity: Any,
            source: MissionSourceEnum,
            vararg groups: Class<*>
        ): CompletenessForStatsEntity {
            val result = validateStatic(entity, *groups)
            return if (result.status == CompletenessForStatsStatusEnum.INCOMPLETE) {
                result.copy(sources = listOf(source))
            } else {
                result
            }
        }
    }
}
