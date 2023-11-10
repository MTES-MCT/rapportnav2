package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionStatusModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionStatusRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionStatusRepository (
    private val dbActionStatusRepository: IDBActionStatusRepository,
    private val mapper: ObjectMapper,
) : INavActionStatusRepository {

    override fun findAllByMissionId(missionId: Int): List<ActionStatusModel> {
        return dbActionStatusRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionStatusModel> {
        return dbActionStatusRepository.findById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionStatusRepository.existsById(id)
    }
    @Transactional
    override fun deleteById(id: UUID) {
        return dbActionStatusRepository.deleteById(id)
    }

    @Transactional
    override fun save(statusAction: ActionStatusEntity): ActionStatusModel {
        return try {
            val statusActionModel = ActionStatusModel.fromActionStatusEntity(statusAction)
            dbActionStatusRepository.save(statusActionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action status", e)
        }
    }

}
