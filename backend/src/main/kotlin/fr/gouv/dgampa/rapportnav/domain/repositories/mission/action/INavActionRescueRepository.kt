package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import ActionRescueEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRescueModel
import java.util.*

interface INavActionRescueRepository {

    fun findAllByMissionId(missionId: Int): List<ActionRescueModel>

    fun findById(id: UUID): Optional<ActionRescueModel>

    fun save(rescueModel: ActionRescueEntity): ActionRescueModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
