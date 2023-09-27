package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigation


interface IControlNavigationRepository {
    fun findAllByMissionId( missionId: Int): List<ControlNavigation>
}
