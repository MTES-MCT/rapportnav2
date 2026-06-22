package fr.gouv.dgampa.rapportnav.domain.validation

import java.time.Instant

/**
 * An immutable, versioned set of required-field validation rules.
 *
 * Each policy defines the complete ruleset that applies to missions
 * starting on or after [appliesFrom]. Policies build incrementally
 * on previous versions — see [ValidationPolicies].
 */
data class ValidationPolicy(
    val version: Int,
    val label: String,
    val appliesFrom: Instant,
    val rules: Map<Class<*>, List<RequiredFieldsValidator.Rule<*>>>
)
