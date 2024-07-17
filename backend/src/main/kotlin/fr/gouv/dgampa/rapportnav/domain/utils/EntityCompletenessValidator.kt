package fr.gouv.dgampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats

object EntityCompletenessValidator {

    /**
     * Checks whether the given entity is complete for statistics based on the presence of mandatory fields
     * and their corresponding conditions specified by the [MandatoryForStats] annotation.
     *
     * @param entity The entity to be checked for completeness.
     * @return `true` if the entity is complete for statistics, `false` otherwise.
     */
    fun isCompleteForStats(entity: Any): Boolean {
        val clazz = entity::class.java
        val fields = clazz.declaredFields
        for (field in fields) {
            if (field.isAnnotationPresent(MandatoryForStats::class.java)) {
                field.isAccessible = true
                val value = field[entity]
                val annotation = field.getAnnotation(MandatoryForStats::class.java)
                val dependentFieldValues = annotation.enableIf
                if (dependentFieldValues.isNotEmpty()) {
                    for (dependentFieldValue in dependentFieldValues) {
                        val fieldValue = getNestedFieldValue(entity, dependentFieldValue.field)
                        if (dependentFieldValue.value.contains(fieldValue)) {
                            if (value == null || (value.toString().isEmpty())
                            ) {
                                return false
                            }
                            break
                        } else {
                            break
                        }
                    }
                } else {
                    if (value == null || (value.toString().isEmpty())) {
                        return false
                    }
                }
            }
        }
        return true
    }


    /**
     * This was necessary for nested fields coming through the @MandatoryForStats annotation
     * Recursively resolves all fields until having the last value
     * Could be smarter but it works
     */
    private fun getNestedFieldValue(entity: Any, fieldPath: String): String? {
        var currentValue: Any? = entity
        val fieldNames = fieldPath.split('.')
        for (fieldName in fieldNames) {
            currentValue = getFieldValue(currentValue, fieldName)
            if (currentValue == null) {
                // If any intermediate field value is null, return null
                return null
            }
        }
        return currentValue.toString()
    }

    private fun getFieldValue(entity: Any?, fieldName: String): Any? {
        if (entity == null) {
            return null
        }
        val field = entity.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(entity)
    }
}
