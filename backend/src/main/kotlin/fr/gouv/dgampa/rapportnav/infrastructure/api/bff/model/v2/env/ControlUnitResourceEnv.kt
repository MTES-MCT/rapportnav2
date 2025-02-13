package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType

data class ControlUnitResourceEnv(
    val id: Int,
    val controlUnit: ControlUnit,
    val controlUnitId: Int,
    val isArchived: Boolean,
    val name: String,
    val note: String? = null,
    val photo: ByteArray? = byteArrayOf(),
    val station: StationData,
    val stationId: Int,
    val type: ControlUnitResourceType,
)
