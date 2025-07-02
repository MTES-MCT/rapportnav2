package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action


import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionActionRepository: JpaRepository<MissionActionModel, UUID> {

    fun findAllByMissionId(missionId: Int): List<MissionActionModel>

    fun findAllByOwnerId(ownerId: UUID): List<MissionActionModel>

    override fun findById(id: UUID): Optional<MissionActionModel>

    fun save(missionActionEntity: MissionNavActionEntity): MissionActionModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
