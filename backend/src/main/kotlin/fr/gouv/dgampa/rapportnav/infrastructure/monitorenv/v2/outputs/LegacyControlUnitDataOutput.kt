package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity

data class LegacyControlUnitDataOutput(
    val id: Int,
    val administration: String,
    val isArchived: Boolean,
    val name: String,
    val resources: List<LegacyControlUnitResourceDataOutput>,
) {

    fun toLegacyControlUnitEntity(): LegacyControlUnitEntity
    {
        return LegacyControlUnitEntity(
            id = id,
            administration =  administration,
            isArchived = isArchived,
            name = name,
            resources = resources.map { it.toLegacyControlUnitResourceEntity(controlUnitId = id) }.toMutableList()

        )
    }
}
