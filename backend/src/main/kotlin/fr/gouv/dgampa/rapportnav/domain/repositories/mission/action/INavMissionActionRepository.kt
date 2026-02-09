package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.ActionModel
import java.util.*

interface INavMissionActionRepository {
    fun findByMissionId(missionId: Int): List<ActionModel>

    fun findByOwnerId(ownerId: UUID): List<ActionModel>

    fun findById(id: UUID): Optional<ActionModel>

    fun save(action: ActionModel): ActionModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
