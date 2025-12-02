package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import java.util.Locale

enum class MissionPassengerOrganization {
    AFF_MAR,
    ESPMER,
    LYCEE,
    OTHER,
    UNKNOWN
}

fun MissionPassengerOrganization?.toStringOrNull(): String? {
    return this?.name
}

fun mapStringToMissionPassengerOrganization(value: String?): MissionPassengerOrganization? {
    if (value != null) {
        return when (value.uppercase(Locale.getDefault())) {
            "AFF_MAR" -> MissionPassengerOrganization.AFF_MAR
            "ESPMER" -> MissionPassengerOrganization.ESPMER
            "LYCEE" -> MissionPassengerOrganization.LYCEE
            "OTHER" -> MissionPassengerOrganization.OTHER
            else -> MissionPassengerOrganization.UNKNOWN
        }
    }
    return null
}
