package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationRulesModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlNavigationRulesRepository: JpaRepository<ControlNavigationRulesModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlNavigationRulesModel>
}
