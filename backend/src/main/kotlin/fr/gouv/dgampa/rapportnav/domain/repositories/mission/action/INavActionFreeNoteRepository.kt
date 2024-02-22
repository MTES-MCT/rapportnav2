package fr.gouv.dgampa.rapportnav.domain.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionFreeNoteModel
import java.util.*

interface INavActionFreeNoteRepository {

    fun findAllByMissionId(missionId: Int): List<ActionFreeNoteModel>

    fun deleteById(id: UUID)

    fun save(freeNoteAction: ActionFreeNoteEntity): ActionFreeNoteModel

    fun findById(id: UUID): Optional<ActionFreeNoteModel>

    fun existsById(id: UUID): Boolean
}
