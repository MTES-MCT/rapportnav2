package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRescueRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionRescueModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionRescueRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionRescueRepository(
    private val dbActionRescueRepository: IDBActionRescueRepository,
) : INavActionRescueRepository {
    override fun findAllByMissionId(missionId: String): List<ActionRescueModel> {
        return dbActionRescueRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionRescueModel> {
        return dbActionRescueRepository.findById(id)
    }

    @Transactional
    override fun save(rescueAction: ActionRescueEntity): ActionRescueModel {
        return try {
            val rescueModel = ActionRescueModel.fromActionRescueEntity(rescueAction)
            dbActionRescueRepository.save(rescueModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionRescue='${rescueAction.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionRescue='${rescueAction.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionRescueRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionRescueRepository.existsById(id)
    }
}
