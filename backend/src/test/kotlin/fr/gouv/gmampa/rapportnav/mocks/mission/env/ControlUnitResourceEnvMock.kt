package fr.gouv.gmampa.rapportnav.mocks.mission.env

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnit
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.StationData

object ControlUnitResourceEnvMock {
    fun create(
        id: Int = 1,
        controlUnit: ControlUnit = ControlUnitMock.create(),
        controlUnitId: Int = 1,
        isArchived: Boolean = false,
        name: String = "Véhicule tout terrain",
        note: String? = null,
        photo: ByteArray? = byteArrayOf(),
        station: StationData = StationData(id = 1, name = "name", latitude = 0.0, longitude = 0.0),
        stationId: Int = 1,
        type: ControlUnitResourceType = ControlUnitResourceType.SUPPORT_SHIP,
    ): ControlUnitResourceEnv {
        return ControlUnitResourceEnv(
            id = id,
            controlUnit = controlUnit,
            controlUnitId = controlUnitId,
            isArchived = isArchived,
            name = name,
            note = note,
            photo = photo,
            stationId = stationId,
            type = type,
            station = station
        )
    }
}
