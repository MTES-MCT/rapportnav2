package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType as LegacyControlUnitResourceType

data class LegacyControlUnitResourceDataOutput(
    val id: Int,
    val controlUnitId: Int,
    val name: String,
    val type: ControlUnitResourceType,
) {

    fun toLegacyControlUnitResourceEntity(controlUnitId: Int): LegacyControlUnitResourceEntity
    {
        return LegacyControlUnitResourceEntity(
            id = id,
            name = name,
            controlUnitId = controlUnitId,
            type = LegacyControlUnitResourceType.valueOf(type.name)
        )
    }
}
