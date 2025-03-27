package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionRepository: JpaRepository<MissionModel, UUID> {

    fun save(entity: MissionNavEntity): MissionModel
}
