package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status

import java.util.*

enum class ActionStatusType {
    NAVIGATING,
    ANCHORED,
    DOCKED,
    UNAVAILABLE,
    UNKNOWN
}

// the following are used in conjunction with annotations (like @MandatoryForStats)
// annotations don't allow code like "ActionStatusType.DOCKED.toString()"
// hence these two const val
const val DOCKED_STATUS_AS_STRING: String = "DOCKED"
const val UNAVAILABLE_STATUS_AS_STRING: String = "UNAVAILABLE"

fun mapStringToActionStatusType(value: String): ActionStatusType {
    return when (value.uppercase(Locale.getDefault())) {
        "NAVIGATING" -> ActionStatusType.NAVIGATING
        "ANCHORED" -> ActionStatusType.ANCHORED
        "DOCKED" -> ActionStatusType.DOCKED
        "UNAVAILABLE" -> ActionStatusType.UNAVAILABLE
        else -> ActionStatusType.UNKNOWN
    }
}

fun mapActionStatusTypeToHumanString(value: ActionStatusType? = null): String {
    return when (value) {
        ActionStatusType.NAVIGATING -> "Navigation"
        ActionStatusType.ANCHORED -> "Mouillage"
        ActionStatusType.DOCKED -> "Présence à quai"
        ActionStatusType.UNAVAILABLE -> "Indisponibilité"
        else -> "Inconnu"
    }
}
