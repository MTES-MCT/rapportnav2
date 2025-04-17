package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionFreeNoteModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBActionFreeNoteRepository: JpaRepository<ActionFreeNoteModel, UUID> {
    fun findAllByMissionId(missionId: String): List<ActionFreeNoteModel>
    override fun findById(id: UUID): Optional<ActionFreeNoteModel>
    override fun deleteById(id: UUID)
    fun save(freeNoteAction: ActionFreeNoteEntity): ActionFreeNoteModel
    override fun existsById(id: UUID): Boolean
}
