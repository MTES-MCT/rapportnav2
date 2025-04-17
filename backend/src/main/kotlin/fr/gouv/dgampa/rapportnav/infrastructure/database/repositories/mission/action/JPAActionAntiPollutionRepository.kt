package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionAntiPollutionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionAntiPollutionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionAntiPollutionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionAntiPollutionRepository(
    private val dbActionAntipollutionRepository: IDBActionAntiPollutionRepository,
) : INavActionAntiPollutionRepository {
    override fun findAllByMissionId(missionId: String): List<ActionAntiPollutionModel> {
        return dbActionAntipollutionRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionAntiPollutionModel> {
        return dbActionAntipollutionRepository.findById(id)
    }

    @Transactional
    override fun save(antiPollutionEntity: ActionAntiPollutionEntity): ActionAntiPollutionModel {
        return try {
            val antiPollutionModel = ActionAntiPollutionModel.fromAntiPollutionEntity(antiPollutionEntity)
            dbActionAntipollutionRepository.save(antiPollutionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionAntiPollution='${antiPollutionEntity.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionAntiPollution='${antiPollutionEntity.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionAntipollutionRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionAntipollutionRepository.existsById(id)
    }
}
