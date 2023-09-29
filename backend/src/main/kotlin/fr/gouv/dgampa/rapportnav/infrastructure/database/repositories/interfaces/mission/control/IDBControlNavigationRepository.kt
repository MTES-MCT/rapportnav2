package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlNavigationModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBControlNavigationRepository: JpaRepository<ControlNavigationModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ControlNavigationModel>
}
