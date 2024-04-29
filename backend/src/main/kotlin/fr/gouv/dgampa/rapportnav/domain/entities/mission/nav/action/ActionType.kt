package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import java.util.*

enum class ActionType {
    CONTROL,
    SURVEILLANCE,
    CONTACT,
    NOTE,
    STATUS,
    RESCUE,
    OTHER,
    NAUTICAL_EVENT,
    ANTI_POLLUTION,
    BAAEM_PERMANENCE,
    VIGIMER,
    PUBLIC_ORDER,
    REPRESENTATION,
    ILLEGAL_IMMIGRATION
}

fun mapStringToActionType(value: String): ActionType {
    return when (value.uppercase(Locale.getDefault())) {
        "CONTROL" -> ActionType.CONTROL
        "SURVEILLANCE" -> ActionType.SURVEILLANCE
        "CONTACT" -> ActionType.CONTACT
        "NOTE" -> ActionType.NOTE
        "STATUS" -> ActionType.STATUS
        "RESCUE" -> ActionType.RESCUE
        "NAUTICAL_EVENT" -> ActionType.NAUTICAL_EVENT
        "ANTI_POLLUTION" -> ActionType.ANTI_POLLUTION
        "BAAEM_PERMANENCE" -> ActionType.BAAEM_PERMANENCE
        "VIGIMER" -> ActionType.VIGIMER
        "ILLEGAL_IMMIGRATION" -> ActionType.ILLEGAL_IMMIGRATION
        "PUBLIC_ORDER" -> ActionType.PUBLIC_ORDER
        "REPRESENTATION" -> ActionType.REPRESENTATION
        else -> ActionType.OTHER
    }
}
