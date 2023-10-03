package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionStatusModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionStatusRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class JPAActionStatusRepository (
    private val dbActionStatusRepository: IDBActionStatusRepository,
    private val mapper: ObjectMapper,
) : INavActionStatusRepository {

    override fun findAllByMissionId(missionId: Int): List<ActionStatus> {
        return dbActionStatusRepository.findAllByMissionId(missionId).map { it.toActionStatus() }
    }
    @Transactional
    override fun save(statusAction: ActionStatus): ActionStatus {
        return try {
            val statusActionModel = ActionStatusModel.fromActionStatus(statusAction, mapper)
//            dbUserRepository.save(statusActionModel).toUser(mapper)
            dbActionStatusRepository.save(statusActionModel).toActionStatus()
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error adding user", e)
        }
    }


}
