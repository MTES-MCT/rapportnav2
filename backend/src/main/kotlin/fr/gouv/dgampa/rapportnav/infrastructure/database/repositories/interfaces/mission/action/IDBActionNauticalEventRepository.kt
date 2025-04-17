package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionNauticalEventEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionNauticalEventModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionNauticalEventRepository: JpaRepository<ActionNauticalEventModel, UUID> {

    fun findAllByMissionId(missionId: String): List<ActionNauticalEventModel>

    override fun findById(id: UUID): Optional<ActionNauticalEventModel>

    fun save(rescueModel: ActionNauticalEventEntity): ActionNauticalEventModel

    override fun deleteById(id: UUID)

    override fun existsById(id: UUID): Boolean
}
