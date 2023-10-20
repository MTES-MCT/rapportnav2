package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionStatusModel
import java.util.*

interface INavActionStatusRepository {
    fun findAllByMissionId( missionId: Int): List<ActionStatus>

    fun findById(id: UUID): ActionStatusModel

    fun existsById(id: UUID): Boolean

    fun save(statusAction: ActionStatus): ActionStatus

}
