package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBActionRepository: JpaRepository<ActionModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ActionModel>
}
