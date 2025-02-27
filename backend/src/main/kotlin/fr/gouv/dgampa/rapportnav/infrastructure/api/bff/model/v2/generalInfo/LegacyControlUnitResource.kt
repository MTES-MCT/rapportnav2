package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity

data class LegacyControlUnitResource(
    val id: Int,
    val name: String,
    val controlUnitId: Int
) {
    fun toLegacyControlUnitResourceEntity(): LegacyControlUnitResourceEntity {
        return LegacyControlUnitResourceEntity(
            id = id,
            name = name,
            controlUnitId = controlUnitId
        )
    }
}
