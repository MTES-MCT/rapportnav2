package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.BaseMissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.utils.MissionDateUtils
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.Instant
import java.time.LocalDate
import java.util.*

/**
 * Validator for WithinMissionDateRange constraint.
 * Validates that entity dates fall within the mission date range.
 *
 * This validator auto-fetches mission dates from the database when injected via
 * Spring's LocalValidatorFactoryBean. In non-Spring contexts (tests, etc.),
 * getMissionDates will be null and validation is skipped gracefully.
 *
 * Supports both Instant and LocalDate field types.
 *
 * Returns true (valid) if:
 * - Entity is null
 * - Mission dates cannot be determined (skip validation)
 * - Entity start date is null (nothing to validate)
 * - Entity dates are within mission date range
 */
class WithinMissionDateRangeValidator(
    private val getMissionDates: GetMissionDates? = null
) : ConstraintValidator<WithinMissionDateRange, Any> {

    private lateinit var startField: String
    private lateinit var endField: String

    override fun initialize(annotation: WithinMissionDateRange) {
        startField = annotation.startField
        endField = annotation.endField
    }

    override fun isValid(entity: Any?, context: ConstraintValidatorContext): Boolean {
        if (entity == null) return true

        var missionStart: Instant? = null
        var missionEnd: Instant? = null

        // Auto-fetch mission dates from database (Spring context via LocalValidatorFactoryBean)
        if (getMissionDates != null) {
            val missionId = ReflectionFieldUtils.getFieldValue(entity, "missionId") as? Int
            val missionIdUUID = ReflectionFieldUtils.getFieldValue(entity, "missionIdUUID") as? UUID
            val ownerId = ReflectionFieldUtils.getFieldValue(entity, "ownerId") as? UUID
            val actionType = ReflectionFieldUtils.getFieldValue(entity, "actionType") as? ActionType

            val dates = when {
                missionId != null -> {
                    val inquiryId = if (actionType == ActionType.INQUIRY) ownerId else null
                    getMissionDates.execute(missionId, ownerId, inquiryId)
                }
                missionIdUUID != null -> {
                    getMissionDates.execute(null, missionIdUUID, null)
                }
                else -> null
            }
            missionStart = dates?.startDateTimeUtc
            missionEnd = dates?.endDateTimeUtc
        }

        // If no mission dates available, skip validation
        if (missionStart == null) return true

        // Get entity dates - use interface for BaseMissionActionEntity implementations
        if (entity is BaseMissionActionEntity) {
            return MissionDateUtils.isWithinMissionDates(
                entity.startDateTimeUtc, entity.endDateTimeUtc, missionStart, missionEnd
            )
        }

        // Try to get dates using configured field names
        val startValue = ReflectionFieldUtils.getFieldValue(entity, startField)
        val endValue = ReflectionFieldUtils.getFieldValue(entity, endField)

        // Handle different date types using MissionDateUtils
        return when (startValue) {
            is Instant -> MissionDateUtils.isWithinMissionDates(
                startValue, endValue as? Instant, missionStart, missionEnd
            )
            is LocalDate -> MissionDateUtils.isWithinMissionDates(
                startValue, endValue as? LocalDate, missionStart, missionEnd
            )
            null -> true // No start date, skip validation
            else -> true // Unknown type, skip validation
        }
    }

}
