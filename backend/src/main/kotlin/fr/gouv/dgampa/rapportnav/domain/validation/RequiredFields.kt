package fr.gouv.dgampa.rapportnav.domain.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Generic class-level constraint that validates required fields.
 *
 * Rules are defined in RequiredFieldsValidator based on the entity class.
 * This replaces @MandatoryForStats with a single, unified annotation.
 *
 * Usage:
 * ```
 * @RequiredFields(groups = [ValidateWhenMissionFinished::class])
 * class MissionNavActionEntity(...)
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RequiredFieldsValidator::class])
annotation class RequiredFields(
    val message: String = "Champs requis manquants",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
