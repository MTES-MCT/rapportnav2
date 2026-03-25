package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.BaseMissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.utils.MissionDateUtils
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.util.*

/**
 * Validator for WithinMissionDateRange constraint.
 * Validates that entity dates fall within the mission date range.
 *
 * This validator is Spring-aware and auto-fetches mission dates from the database
 * using the entity's missionId or missionIdUUID.
 *
 * Supports both Instant and LocalDate field types. When entity has LocalDate fields,
 * mission dates (Instant) are converted to LocalDate for comparison.
 *
 * For entities implementing BaseMissionActionEntity, uses the interface properties
 * (startDateTimeUtc, endDateTimeUtc) which handles different internal field names
 * (e.g., actionDatetimeUtc in MissionFishActionEntity).
 *
 * Returns true (valid) if:
 * - Entity is null
 * - Mission dates cannot be determined (skip validation)
 * - Entity start date is null (nothing to validate)
 * - Entity dates are within mission date range
 */
@Component
class WithinMissionDateRangeValidator : ConstraintValidator<WithinMissionDateRange, Any> {

    @Autowired(required = false)
    private var getMissionDates: GetMissionDates? = null

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

        // Primary: Auto-fetch mission dates from database (Spring context)
        if (getMissionDates != null) {
            val missionId = getFieldValue(entity, "missionId") as? Int
            val missionIdUUID = getFieldValue(entity, "missionIdUUID") as? UUID
            val ownerId = getFieldValue(entity, "ownerId") as? UUID
            val actionType = getFieldValue(entity, "actionType") as? ActionType

            val dates = when {
                missionId != null -> {
                    val inquiryId = if (actionType == ActionType.INQUIRY) ownerId else null
                    getMissionDates?.execute(missionId, ownerId, inquiryId)
                }
                missionIdUUID != null -> {
                    // missionIdUUID is used as ownerId for nav missions
                    getMissionDates?.execute(null, missionIdUUID, null)
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
        val startValue = getFieldValue(entity, startField)
        val endValue = getFieldValue(entity, endField)

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

    private fun getFieldValue(obj: Any, fieldName: String): Any? {
        return try {
            val field = findField(obj::class.java, fieldName)
            field?.isAccessible = true
            field?.get(obj)
        } catch (e: Exception) {
            null
        }
    }

    private fun findField(clazz: Class<*>?, fieldName: String): java.lang.reflect.Field? {
        if (clazz == null) return null
        return try {
            clazz.getDeclaredField(fieldName)
        } catch (e: NoSuchFieldException) {
            // Search in parent class
            findField(clazz.superclass, fieldName)
        }
    }
}
