package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.BaseMissionActionEntity
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.Instant
import java.time.LocalDate

/**
 * Validator for EndAfterStart constraint.
 * Validates that the end date is after the start date.
 *
 * Supports both Instant and LocalDate field types.
 *
 * For entities implementing BaseMissionActionEntity, uses the interface properties
 * (startDateTimeUtc, endDateTimeUtc) which handles different internal field names
 * (e.g., actionDatetimeUtc in MissionFishActionEntity).
 *
 * Returns true (valid) if:
 * - Entity is null
 * - Start date is null (nothing to compare)
 * - End date is null (nothing to compare)
 * - End date is after start date
 */
class EndAfterStartValidator : ConstraintValidator<EndAfterStart, Any> {

    private lateinit var startField: String
    private lateinit var endField: String

    override fun initialize(annotation: EndAfterStart) {
        startField = annotation.startField
        endField = annotation.endField
    }

    override fun isValid(entity: Any?, context: ConstraintValidatorContext): Boolean {
        if (entity == null) return true

        // Use interface properties for BaseMissionActionEntity implementations
        if (entity is BaseMissionActionEntity) {
            val start = entity.startDateTimeUtc ?: return true
            val end = entity.endDateTimeUtc ?: return true
            return end.isAfter(start)
        }

        // Fallback to reflection for other entities
        val startValue = getFieldValue(entity, startField) ?: return true
        val endValue = getFieldValue(entity, endField) ?: return true

        // Handle different date types
        return when {
            startValue is Instant && endValue is Instant -> endValue.isAfter(startValue)
            startValue is LocalDate && endValue is LocalDate -> endValue.isAfter(startValue)
            else -> true // Unknown types, skip validation
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
