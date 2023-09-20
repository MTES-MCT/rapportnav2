package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction

interface INavActionRepository {
    fun findAllByMissionId( missionId: Int): List<NavAction>
}
