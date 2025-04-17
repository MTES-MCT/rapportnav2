package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionBAAEMRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionBAAEMPermanenceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionBAAEMPermanenceRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionBAAEMPermanenceRepository(
    private val dbActionBAAEMPermanenceRepository: IDBActionBAAEMPermanenceRepository,
) : INavActionBAAEMRepository {
    override fun findAllByMissionId(missionId: String): List<ActionBAAEMPermanenceModel> {
        return dbActionBAAEMPermanenceRepository.findAllByMissionId(missionId = missionId)
    }

    override fun findById(id: UUID): Optional<ActionBAAEMPermanenceModel> {
        return dbActionBAAEMPermanenceRepository.findById(id = id)
    }

    @Transactional
    override fun save(permanenceBAAEM: ActionBAAEMPermanenceEntity): ActionBAAEMPermanenceModel {
        return try {
            val baaemModel = ActionBAAEMPermanenceModel.fromBAAEMPermanenceEntity(permanenceBAAEM)
            dbActionBAAEMPermanenceRepository.save(baaemModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionBAAEM='${permanenceBAAEM.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionBAAEM='${permanenceBAAEM.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionBAAEMPermanenceRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionBAAEMPermanenceRepository.existsById(id)
    }
}
