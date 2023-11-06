package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionStatusModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionStatusRepository: JpaRepository<ActionStatusModel, UUID> {
    fun findAllByMissionId(missionId: Int): List<ActionStatusModel>

    override fun findById(id: UUID): Optional<ActionStatusModel>

    fun existsById(id: UUID?): Boolean

    override fun deleteById(id: UUID)

    fun save(statusAction: ActionStatusEntity): ActionStatusModel

}
