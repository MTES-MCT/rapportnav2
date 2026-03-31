package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.PortEntity

data class PortDataOutput(
    val locode: String,
    val name: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val region: String? = null,
) {
    fun toPortEntity() =
        PortEntity(
            locode = locode,
            name = name,
            latitude = latitude,
            longitude = longitude,
            region = region,
        )
}
