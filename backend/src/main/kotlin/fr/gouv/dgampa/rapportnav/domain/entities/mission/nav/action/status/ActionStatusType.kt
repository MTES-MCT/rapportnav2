package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status

import java.util.*

enum class ActionStatusType {
    NAVIGATING,
    ANCHORED,
    DOCKED,
    UNAVAILABLE,
    UNKNOWN
}

fun mapStringToActionStatusType(value: String): ActionStatusType {
    return when (value.uppercase(Locale.getDefault())) {
        "NAVIGATING" -> ActionStatusType.NAVIGATING
        "ANCHORED" -> ActionStatusType.ANCHORED
        "DOCKED" -> ActionStatusType.DOCKED
        "UNAVAILABLE" -> ActionStatusType.UNAVAILABLE
        else -> ActionStatusType.UNKNOWN
    }
}
