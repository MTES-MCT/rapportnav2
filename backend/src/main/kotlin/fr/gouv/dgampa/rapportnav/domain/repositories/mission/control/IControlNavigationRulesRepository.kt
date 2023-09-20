package fr.gouv.dgampa.rapportnav.domain.repositories.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationRules


interface IControlNavigationRulesRepository {
    fun findAllByMissionId( missionId: Int): List<ControlNavigationRules>
}
