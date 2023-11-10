package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

enum class FormalNoticeEnum {
    YES,
    NO,
    PENDING,
}

fun FormalNoticeEnum?.toStringOrNull(): String? {
    return this?.name
}
