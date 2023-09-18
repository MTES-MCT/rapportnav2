package fr.gouv.dgampa.rapportnav.domain.repositories.control

import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlNavigationRules


interface IControlNavigationRulesRepository {
    fun findAllByMissionId( missionId: Int): List<ControlNavigationRules>
}
