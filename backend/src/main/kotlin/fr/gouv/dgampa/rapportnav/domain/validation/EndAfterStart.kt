package fr.gouv.dgampa.rapportnav.domain.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Class-level constraint: validates that endDateTimeUtc > startDateTimeUtc
 *
 * Usage:
 * @EndAfterStart(groups = [ValidateThrowsBeforeSave::class])
 * class MyActionEntity(...)
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EndAfterStartValidator::class])
annotation class EndAfterStart(
    val message: String = "La date de fin doit être postérieure à la date de début",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val startField: String = "startDateTimeUtc",
    val endField: String = "endDateTimeUtc"
)
