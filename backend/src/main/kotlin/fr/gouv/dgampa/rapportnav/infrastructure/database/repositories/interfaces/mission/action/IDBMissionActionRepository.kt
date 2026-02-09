package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action


import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.ActionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionActionRepository: JpaRepository<ActionModel, UUID> {

    fun findAllByMissionId(missionId: Int): List<ActionModel>

    fun findAllByOwnerId(ownerId: UUID): List<ActionModel>

    override fun findById(id: UUID): Optional<ActionModel>

    fun save(missionActionEntity: NavActionEntity): ActionModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
