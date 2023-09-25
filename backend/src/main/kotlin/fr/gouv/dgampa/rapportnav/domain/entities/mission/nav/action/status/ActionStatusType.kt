package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status

import java.util.*

enum class ActionStatusType {
    NAVIGATING,
    ANCHORING,
    DOCKED,
    UNAVAILABLE,
}

fun mapStringToActionStatusType(value: String): ActionStatusType {
    return when (value.uppercase(Locale.getDefault())) {
        "NAVIGATING" -> ActionStatusType.NAVIGATING
        "ANCHORING" -> ActionStatusType.ANCHORING
        "DOCKED" -> ActionStatusType.DOCKED
        "UNAVAILABLE" -> ActionStatusType.UNAVAILABLE
        else -> ActionStatusType.UNAVAILABLE
    }
}
