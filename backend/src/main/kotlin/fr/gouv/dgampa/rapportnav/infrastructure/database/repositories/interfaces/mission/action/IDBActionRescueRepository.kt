package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import ActionRescueEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRescueModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionRescueRepository: JpaRepository<ActionRescueModel, UUID>{

    fun findAllByMissionId(missionId: Int): List<ActionRescueModel>

    override fun findById(id: UUID): Optional<ActionRescueModel>

    fun save(rescueModel: ActionRescueEntity): ActionRescueModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
