package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import java.util.*

enum class ActionType {
    CONTROL,
    SURVEILLANCE,
    CONTACT,
    NOTE,
    STATUS,
    OTHER
}

fun mapStringToActionType(value: String): ActionType {
    return when (value.uppercase(Locale.getDefault())) {
        "CONTROL" -> ActionType.CONTROL
        "SURVEILLANCE" -> ActionType.SURVEILLANCE
        "CONTACT" -> ActionType.CONTACT
        "NOTE" -> ActionType.NOTE
        "STATUS" -> ActionType.STATUS
        else -> ActionType.OTHER
    }
}
