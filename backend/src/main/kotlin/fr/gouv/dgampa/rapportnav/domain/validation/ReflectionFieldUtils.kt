package fr.gouv.dgampa.rapportnav.domain.validation

/**
 * Shared reflection utilities for constraint validators that need
 * to read fields by name (e.g., configurable start/end date field names).
 */
object ReflectionFieldUtils {

    fun getFieldValue(obj: Any, fieldName: String): Any? {
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
            findField(clazz.superclass, fieldName)
        }
    }
}
