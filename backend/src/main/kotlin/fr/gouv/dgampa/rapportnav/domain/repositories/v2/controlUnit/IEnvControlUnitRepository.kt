package fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitEntity

interface IEnvControlUnitRepository {

    fun findAll(): List<LegacyControlUnitEntity>?

    /** One MonitorEnv control unit with all its resources. Throws when MonitorEnv can't be reached or answers an error. */
    fun findById(controlUnitId: Int): ControlUnitEntity

}
