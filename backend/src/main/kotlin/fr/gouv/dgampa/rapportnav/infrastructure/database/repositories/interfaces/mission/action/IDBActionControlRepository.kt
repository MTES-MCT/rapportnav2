package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBActionControlRepository: JpaRepository<ActionControlModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ActionControlModel>
}
