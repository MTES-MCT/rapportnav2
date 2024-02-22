package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionFreeNoteRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionFreeNoteModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionFreeNoteRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionFreeNoteRepository(
    private val dbActionFreeNoteRepository: IDBActionFreeNoteRepository,
    private val mapper: ObjectMapper
) : INavActionFreeNoteRepository
{

    override fun findAllByMissionId(missionId: Int): List<ActionFreeNoteModel> {
        return dbActionFreeNoteRepository.findAllByMissionId(missionId)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        return dbActionFreeNoteRepository.deleteById(id)
    }

    @Transactional
    override fun save(freeNoteAction: ActionFreeNoteEntity): ActionFreeNoteModel {
        return try {
            val freeNoteModel = ActionFreeNoteModel.fromActionFreeNote(freeNoteAction, mapper)
            dbActionFreeNoteRepository.save(freeNoteModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action free note", e)
        }
    }

    override fun findById(id: UUID): Optional<ActionFreeNoteModel> {
        return dbActionFreeNoteRepository.findById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionFreeNoteRepository.existsById(id)
    }

}
