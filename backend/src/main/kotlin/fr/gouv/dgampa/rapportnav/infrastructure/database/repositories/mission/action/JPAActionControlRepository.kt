package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionControlRepository (
    private val dbActionModelRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : INavActionControlRepository {

    override fun findAllByMissionId(missionId: Int): List<ActionControl> {
        return dbActionModelRepository.findAllByMissionId(missionId).map { it.toActionControl() }
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionModelRepository.existsById(id)
    }

    @Transactional
    override fun save(controlAction: ActionControl): ActionControl {
        return try {
            val controlActionModel = ActionControlModel.fromActionControl(controlAction, mapper)
            dbActionModelRepository.save(controlActionModel).toActionControl()
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action control", e)
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        try {
            dbActionModelRepository.deleteById(id)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error deleting control", e)
        }
    }


}
