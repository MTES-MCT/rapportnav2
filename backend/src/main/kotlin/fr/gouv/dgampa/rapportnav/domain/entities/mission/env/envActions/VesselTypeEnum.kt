package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

enum class VesselTypeEnum {
    FISHING,
    SAILING,
    MOTOR,
    COMMERCIAL,
    SAILING_LEISURE,
}

fun mapStringToVesselType(value: String?): VesselTypeEnum? {
    if (value != null) {
        return try {
            // Use the enum valueOf() method to map the string to the enum member
            enumValueOf<VesselTypeEnum>(value.uppercase())
        } catch (e: IllegalArgumentException) {
            // Handle the case where the string does not match any enum value
            null
        }
    }
    return null
}
