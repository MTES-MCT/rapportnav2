package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

enum class JdpTypeEnum {
    LAND,
    ON_BOARD;

    companion object {
        fun fromString(value: String): JdpTypeEnum? {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
        }
    }
}
