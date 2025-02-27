package fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity

interface IEnvControlUnitRepository {

    fun findAll(): List<LegacyControlUnitEntity>?

    fun findById(id: Int): LegacyControlUnitEntity?
}
