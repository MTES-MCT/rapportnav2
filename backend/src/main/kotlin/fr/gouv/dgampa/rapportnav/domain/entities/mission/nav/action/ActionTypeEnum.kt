package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import java.util.*

enum class ActionTypeEnum {
    CONTROL,
    SURVEILLANCE,
    CONTACT,
    NOTE,
    STATUS,
    OTHER
}

fun mapStringToActionType(value: String): ActionTypeEnum {
    return when (value.uppercase(Locale.getDefault())) {
        "CONTROL" -> ActionTypeEnum.CONTROL
        "SURVEILLANCE" -> ActionTypeEnum.SURVEILLANCE
        "CONTACT" -> ActionTypeEnum.CONTACT
        "NOTE" -> ActionTypeEnum.NOTE
        "STATUS" -> ActionTypeEnum.STATUS
        else -> ActionTypeEnum.OTHER
    }
}
