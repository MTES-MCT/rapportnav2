package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status

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

fun mapActionStatusTypeToHumanString(value: ActionStatusType): String {
    return when (value) {
        ActionStatusType.NAVIGATING -> "Navigation"
        ActionStatusType.ANCHORED -> "Mouillage"
        ActionStatusType.DOCKED -> "Présence à quai"
        ActionStatusType.UNAVAILABLE -> "Indisponibilité"
        else -> "Inconnu"
    }
}
