package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status

import java.util.*

enum class ActionStatusReason {
    MAINTENANCE,
    WEATHER,
    REPRESENTATION,
    ADMINISTRATION,
    HARBOUR_CONTROL,
    TECHNICAL,
    PERSONNEL,
    OTHER
}

fun ActionStatusReason?.toStringOrNull(): String? {
    return this?.name
}

fun mapStringToActionStatusReason(value: String?): ActionStatusReason? {
    if (value != null) {
        return when (value.uppercase(Locale.getDefault())) {
            "MAINTENANCE" -> ActionStatusReason.MAINTENANCE
            "WEATHER" -> ActionStatusReason.WEATHER
            "REPRESENTATION" -> ActionStatusReason.REPRESENTATION
            "ADMINISTRATION" -> ActionStatusReason.ADMINISTRATION
            "HARBOUR_CONTROL" -> ActionStatusReason.HARBOUR_CONTROL
            "TECHNICAL" -> ActionStatusReason.TECHNICAL
            "PERSONNEL" -> ActionStatusReason.PERSONNEL
            "OTHER" -> ActionStatusReason.OTHER
            else -> ActionStatusReason.OTHER
        }
    }
    return null
}
