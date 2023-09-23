package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionStatusModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBActionStatusRepository: JpaRepository<ActionStatusModel, Int> {
//    fun findOngoingStatusForMission(missionId: Int): ActionStatusModel
}
