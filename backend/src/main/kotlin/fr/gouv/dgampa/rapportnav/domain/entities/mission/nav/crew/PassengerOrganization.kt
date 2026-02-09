package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import java.util.Locale

enum class PassengerOrganization {
    AFF_MAR,
    ESPMER,
    LYCEE,
    OTHER,
    UNKNOWN
}

fun PassengerOrganization?.toStringOrNull(): String? {
    return this?.name
}

fun mapStringToMissionPassengerOrganization(value: String?): PassengerOrganization? {
    if (value != null) {
        return when (value.uppercase(Locale.getDefault())) {
            "AFF_MAR" -> PassengerOrganization.AFF_MAR
            "ESPMER" -> PassengerOrganization.ESPMER
            "LYCEE" -> PassengerOrganization.LYCEE
            "OTHER" -> PassengerOrganization.OTHER
            else -> PassengerOrganization.UNKNOWN
        }
    }
    return null
}
