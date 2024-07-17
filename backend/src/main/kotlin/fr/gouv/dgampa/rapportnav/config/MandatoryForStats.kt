package fr.gouv.dgampa.rapportnav.config

import org.springframework.stereotype.Component

annotation class DependentFieldValue(
    val field: String,
    val value: Array<String>
)

/**
 * Annotation to mark fields as mandatory, optionally dependent on other fields and their values.
 * As we are going with partial data saving, our Entities are quite loose and so entity definition is insufficient
 * to check for completeness of data. Hence, this annotation.
 * Completeness is important to mark data as usable for statistics
 *
 * example of usage:
 * - @MandatoryForStats
 * - @MandatoryForStats(
 *     enableIf = [
 *         DependentFieldValue(field = "status", value = arrayOf("42")), // For numeric, convert them to strings
 *         DependentFieldValue(field = "isFinished", value = arrayOf("true")), // For bools, convert them to strings
 *         DependentFieldValue(field = "someEnum", value = arrayOf("X", "Y")) // For enums, use their names as strings
 *         DependentFieldValue(field = "action.isComplete", value = arrayOf("true")) // For nester fields
 *     ]
 * )
 *
 * @param enableIf Array of field of field/value pair to mark conditions enabling the mandatoriness of the field
 */

@Target(AnnotationTarget.FIELD)
@Component
annotation class MandatoryForStats(
    val enableIf: Array<DependentFieldValue> = []
)

