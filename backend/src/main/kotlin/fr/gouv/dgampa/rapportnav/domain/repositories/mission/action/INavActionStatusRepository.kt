package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import java.util.*

interface INavActionStatusRepository {
    fun findAllByMissionId( missionId: Int): List<ActionStatus>

    fun existsById(id: UUID): Boolean

    fun save(statusAction: ActionStatus): ActionStatus

    fun deleteById(id: UUID)
}
