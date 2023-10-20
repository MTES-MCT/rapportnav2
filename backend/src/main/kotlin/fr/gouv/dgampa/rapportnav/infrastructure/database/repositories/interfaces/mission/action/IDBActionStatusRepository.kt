package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionStatusModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionStatusRepository: JpaRepository<ActionStatusModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ActionStatusModel>

    fun findById(id: UUID): ActionStatusModel

    fun existsById(id: UUID): Boolean

    fun save(statusAction: ActionStatus): ActionStatus

}
