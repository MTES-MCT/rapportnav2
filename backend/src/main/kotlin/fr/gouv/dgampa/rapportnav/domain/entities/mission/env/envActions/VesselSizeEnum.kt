package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

/* ktlint-disable enum-entry-name-case */
enum class VesselSizeEnum {
    LESS_THAN_12m,
    FROM_12_TO_24m,
    FROM_24_TO_46m,
    MORE_THAN_46m,
}
/* ktlint-enable enum-entry-name-case */

fun mapStringToVesselSize(value: String?): VesselSizeEnum? {
    if (value != null) {
        return try {
            // Use the enum valueOf() method to map the string to the enum member
            enumValueOf<VesselSizeEnum>(value)
        } catch (e: IllegalArgumentException) {
            // Handle the case where the string does not match any enum value
            null
        }
    }
    return null
}
