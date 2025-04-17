package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionNauticalEventEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionNauticalEventModel
import java.util.*

interface INavActionNauticalEventRepository {
    fun findAllByMissionId(missionId: String): List<ActionNauticalEventModel>
    fun findById(id: UUID): Optional<ActionNauticalEventModel>

    fun save(nauticalEvent: ActionNauticalEventEntity): ActionNauticalEventModel

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean
}
