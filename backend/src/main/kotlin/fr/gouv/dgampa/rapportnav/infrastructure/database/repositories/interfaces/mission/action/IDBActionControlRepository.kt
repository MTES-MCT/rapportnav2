package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionControlRepository: JpaRepository<ActionControlModel, Int> {
    fun findAllByMissionId(missionId: Int): List<ActionControlModel>

    fun existsById(id: UUID): Boolean
    fun save(controlAction: ActionControl): ActionControl

    fun deleteById(id: UUID)
}
