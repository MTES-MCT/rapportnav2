package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status

import java.util.*

enum class ActionStatusReason {
    MAINTENANCE,
    WEATHER,
    REPRESENTATION,
    ADMINISTRATION,
    HARBOUR_CONTROL,
    OTHER
}

fun mapStringToActionStatusReason(value: String?): ActionStatusReason? {
    if (value != null) {
        return when (value.uppercase(Locale.getDefault())) {
            "MAINTENANCE" -> ActionStatusReason.MAINTENANCE
            "WEATHER" -> ActionStatusReason.WEATHER
            "REPRESENTATION" -> ActionStatusReason.REPRESENTATION
            "ADMINISTRATION" -> ActionStatusReason.ADMINISTRATION
            "HARBOUR_CONTROL" -> ActionStatusReason.HARBOUR_CONTROL
            "OTHER" -> ActionStatusReason.OTHER
            else -> ActionStatusReason.OTHER
        }
    }
    return null
}
