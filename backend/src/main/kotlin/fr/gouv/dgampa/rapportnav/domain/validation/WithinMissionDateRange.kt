package fr.gouv.dgampa.rapportnav.domain.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

/**
 * Class-level constraint: validates that entity dates are within mission date range.
 *
 * The validator auto-fetches mission dates from the database using the entity's missionId or missionIdUUID.
 * Supports both Instant and LocalDate field types.
 *
 * Usage for Instant fields (default):
 * @WithinMissionDateRange(groups = [ValidateThrowsBeforeSave::class])
 * class MyActionEntity(var missionId: Int, var startDateTimeUtc: Instant?, var endDateTimeUtc: Instant?)
 *
 * Usage for LocalDate fields:
 * @WithinMissionDateRange(startField = "startDate", endField = "endDate", groups = [ValidateThrowsBeforeSave::class])
 * class MyEntity(var missionId: Int, var startDate: LocalDate, var endDate: LocalDate)
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [WithinMissionDateRangeValidator::class])
annotation class WithinMissionDateRange(
    val message: String = "Les dates doivent être comprises dans les dates de la mission",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val startField: String = "startDateTimeUtc",
    val endField: String = "endDateTimeUtc"
)
