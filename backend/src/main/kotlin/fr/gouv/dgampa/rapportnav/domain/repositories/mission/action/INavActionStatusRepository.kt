package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus

interface INavActionStatusRepository {
    fun findAllByMissionId( missionId: Int): List<ActionStatus>
    fun save(statusAction: ActionStatus): ActionStatus
}
