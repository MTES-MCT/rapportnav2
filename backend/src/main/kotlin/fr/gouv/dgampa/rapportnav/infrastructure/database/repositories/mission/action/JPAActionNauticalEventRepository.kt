package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionNauticalEventEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionNauticalEventRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionNauticalEventModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionNauticalEventRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionNauticalEventRepository(
    private val dbActionNauticalEventRepository: IDBActionNauticalEventRepository,
) : INavActionNauticalEventRepository {
    override fun findAllByMissionId(missionId: Int): List<ActionNauticalEventModel> {
        return dbActionNauticalEventRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionNauticalEventModel> {
        return dbActionNauticalEventRepository.findById(id)
    }

    @Transactional
    override fun save(nauticalEvent: ActionNauticalEventEntity): ActionNauticalEventModel {
        return try {
            val nauticalModel = ActionNauticalEventModel.fromNauticalEventEntity(nauticalEvent)
            dbActionNauticalEventRepository.save(nauticalModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionNauticalEvent='${nauticalEvent.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionNauticalEvent='${nauticalEvent.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionNauticalEventRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionNauticalEventRepository.existsById(id)
    }
}
