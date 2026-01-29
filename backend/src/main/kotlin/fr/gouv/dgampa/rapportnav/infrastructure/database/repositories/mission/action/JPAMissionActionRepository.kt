package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAMissionActionRepository(
    private val dbMissionActionRepository: IDBMissionActionRepository,
) : INavMissionActionRepository {
    override fun findByMissionId(missionId: Int): List<MissionActionModel> {
        return try {
            dbMissionActionRepository.findAllByMissionId(missionId)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionActions for missionId='$missionId'",
                originalException = e
            )
        }
    }

    override fun findByOwnerId(ownerId: UUID): List<MissionActionModel> {
        return try {
            dbMissionActionRepository.findAllByOwnerId(ownerId)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionActions for ownerId='$ownerId'",
                originalException = e
            )
        }
    }

    override fun findById(id: UUID): Optional<MissionActionModel> {
        return try {
            dbMissionActionRepository.findById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionAction with id='$id'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun save(action: MissionActionModel): MissionActionModel {
        return try {
            dbMissionActionRepository.save(action)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save MissionAction='${action.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving MissionAction='${action.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        try {
            dbMissionActionRepository.deleteById(id)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "Unable to delete MissionAction='$id'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to delete MissionAction with id='$id'",
                originalException = e
            )
        }
    }

    override fun existsById(id: UUID): Boolean {
        return try {
            dbMissionActionRepository.existsById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to check existence of MissionAction with id='$id'",
                originalException = e
            )
        }
    }
}
