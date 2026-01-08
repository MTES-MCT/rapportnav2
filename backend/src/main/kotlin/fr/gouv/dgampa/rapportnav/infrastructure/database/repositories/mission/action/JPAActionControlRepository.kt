package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.json.JsonMapper
import java.util.*

@Repository
class JPAActionControlRepository(
    private val dbActionModelRepository: IDBActionControlRepository,
    private val mapper: JsonMapper,
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
    override fun deleteById(id: UUID) {
        return dbActionModelRepository.deleteById(id)
    }

    @Transactional
    override fun save(controlAction: ActionControlEntity): ActionControlModel {
        return try {
            val controlActionModel = ActionControlModel.fromActionControl(controlAction)
            dbActionModelRepository.save(controlActionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionControl='${controlAction.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionControl='${controlAction.id}'",
                originalException = e
            )
        }
    }


}
