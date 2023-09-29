package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

enum class ControlMethod {
    AIR,
    LAND,
    SEA,
}

fun mapStringToControlMethod(value: String?): ControlMethod? {
    if (value != null) {
        return try {
            // Use the enum valueOf() method to map the string to the enum member
            enumValueOf<ControlMethod>(value)
        } catch (e: IllegalArgumentException) {
            // Handle the case where the string does not match any enum value
            null
        }
    }
    return null
}
