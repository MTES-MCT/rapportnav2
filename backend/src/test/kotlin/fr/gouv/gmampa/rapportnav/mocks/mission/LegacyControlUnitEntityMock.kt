package fr.gouv.gmampa.rapportnav.mocks.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity

object LegacyControlUnitEntityMock {
    fun create(
        id: Int = 1,
        administration: String = "DDTM",
        isArchived: Boolean = false,
        name: String = "zodiac abc",
        resources: MutableList<LegacyControlUnitResourceEntity> = mutableListOf(),
        contact: String? = null,
    ): LegacyControlUnitEntity {
        return LegacyControlUnitEntity(
            id,
            administration,
            isArchived,
            name,
            resources,
            contact,
        )
    }
}
