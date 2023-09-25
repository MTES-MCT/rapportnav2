package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction

interface INavActionControlRepository {
    fun findAllByMissionId( missionId: Int): List<NavAction>
}
