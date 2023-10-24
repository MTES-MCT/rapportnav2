package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

enum class ControlResult {
    YES,
    NO,
    NOT_CONTROLLED,
    NOT_CONCERNED
}

fun ControlResult?.toStringOrNull(): String? {
    return this?.name
}

fun stringToControlResult(value: String?): ControlResult? {
    return when (value) {
        "YES" -> ControlResult.YES
        "NO" -> ControlResult.NO
        "NOT_CONTROLLED" -> ControlResult.NOT_CONTROLLED
        "NOT_CONCERNED" -> ControlResult.NOT_CONCERNED
        else -> null
    }
}
