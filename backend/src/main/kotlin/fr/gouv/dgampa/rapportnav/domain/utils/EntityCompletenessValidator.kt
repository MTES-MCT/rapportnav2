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
                        val dependentField = clazz.getDeclaredField(dependentFieldValue.field)
                        dependentField.isAccessible = true
                        val fieldValue = dependentField[entity]?.toString()
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
}
