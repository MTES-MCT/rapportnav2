package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity

data class LegacyControlUnitResourceDataOutput(
    val id: Int,
    val name: String,
) {

    fun toLegacyControlUnitResourceEntity(controlUnitId: Int): LegacyControlUnitResourceEntity
    {
        return LegacyControlUnitResourceEntity(
            id = id,
            name = name,
            controlUnitId = controlUnitId
        )
    }
}
