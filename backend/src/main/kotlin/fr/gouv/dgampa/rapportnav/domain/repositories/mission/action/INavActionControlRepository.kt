package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl

interface INavActionControlRepository {
    fun findAllByMissionId( missionId: Int): List<ActionControl>
}
