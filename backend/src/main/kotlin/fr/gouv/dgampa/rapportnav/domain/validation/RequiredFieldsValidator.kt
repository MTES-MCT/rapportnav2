package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity

/**
 * Validates required fields against policy-defined rules.
 *
 * Rules are defined per entity class in [ValidationPolicies].
 * This is a standalone utility — not a Jakarta ConstraintValidator.
 */
object RequiredFieldsValidator {

    fun validate(entity: Any, rules: Map<Class<*>, List<Rule<*>>>): List<FieldViolation> {
        val entityRules = rules[entity::class.java] ?: return emptyList()
        val violations = mutableListOf<FieldViolation>()

        for (rule in entityRules) {
            if (rule.appliesTo(entity) && rule.isViolated(entity)) {
                violations.add(FieldViolation(rule.field, rule.message))
            }
        }

        return violations
    }

    data class FieldViolation(val field: String, val message: String)

    /**
     * Represents a validation rule for a field.
     */
    sealed class Rule<T>(
        val field: String,
        val message: String,
        val conditionDescription: String
    ) {
        abstract fun appliesTo(entity: Any): Boolean
        abstract fun isViolated(entity: Any): Boolean

        class Always<T>(
            field: String,
            message: String,
            private val getter: (T) -> Any?
        ) : Rule<T>(field, message, "Toujours") {
            override fun appliesTo(entity: Any) = true
            @Suppress("UNCHECKED_CAST")
            override fun isViolated(entity: Any) = getter(entity as T) == null
        }

        class ForActionTypes(
            field: String,
            val actionTypes: List<ActionType>,
            message: String,
            private val getter: (MissionNavActionEntity) -> Any?
        ) : Rule<MissionNavActionEntity>(field, message, "actionType ∈ {${actionTypes.joinToString(", ")}}") {
            override fun appliesTo(entity: Any) = (entity as? MissionNavActionEntity)?.actionType in actionTypes
            @Suppress("UNCHECKED_CAST")
            override fun isViolated(entity: Any) = getter(entity as MissionNavActionEntity) == null
        }

        class Conditional<T>(
            field: String,
            message: String,
            conditionDescription: String,
            private val condition: (T) -> Boolean,
            val relatedActionTypes: List<ActionType>? = null,
            val extraCondition: String? = null,
            private val getter: (T) -> Any?
        ) : Rule<T>(field, message, conditionDescription) {
            @Suppress("UNCHECKED_CAST")
            override fun appliesTo(entity: Any) = condition(entity as T)
            @Suppress("UNCHECKED_CAST")
            override fun isViolated(entity: Any) = getter(entity as T) == null
        }

        companion object {
            fun <T> always(field: String, message: String, getter: (T) -> Any?) =
                Always(field, message, getter)

            fun forActionTypes(field: String, actionTypes: List<ActionType>, message: String, getter: (MissionNavActionEntity) -> Any?) =
                ForActionTypes(field, actionTypes, message, getter)

            fun <T> conditional(
                field: String, message: String, conditionDescription: String,
                condition: (T) -> Boolean,
                relatedActionTypes: List<ActionType>? = null,
                extraCondition: String? = null,
                getter: (T) -> Any?
            ) = Conditional(field, message, conditionDescription, condition, relatedActionTypes, extraCondition, getter)
        }
    }
}
