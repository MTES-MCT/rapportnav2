package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
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

    override fun findAllByMissionId(missionId: Int): List<ActionControlEntity> {
        return dbActionModelRepository.findAllByMissionId(missionId).map { it.toActionControlEntity() }
    }

    override fun findById(id: UUID): Optional<ActionControlModel> {
        return dbActionModelRepository.findById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionModelRepository.existsById(id)
    }

    @Transactional
    override fun save(controlAction: ActionControlEntity): ActionControlModel {
        return try {
            val controlActionModel = ActionControlModel.fromActionControl(controlAction, mapper)
            dbActionModelRepository.save(controlActionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action control", e)
        }
    }


}
