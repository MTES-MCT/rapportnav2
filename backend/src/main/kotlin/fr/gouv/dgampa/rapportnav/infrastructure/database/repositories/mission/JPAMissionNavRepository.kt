package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.IDBMissionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Repository
class JPAMissionNavRepository(
    private val dbRepository: IDBMissionRepository
) : IMissionNavRepository {
    override fun save(model: MissionModel): MissionModel {
        return try {
            dbRepository.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save MissionNav='${model.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving MissionNav='${model.id}'",
                originalException = e
            )
        }
    }

    override fun finById(id: UUID): Optional<MissionModel> {
        return try {
            dbRepository.findById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionNav with id='$id'",
                originalException = e
            )
        }
    }

    override fun findAll(startBeforeDateTime: Instant, endBeforeDateTime: Instant): List<MissionModel?> {
        return try {
            dbRepository.findAllBetweenDates(
                startBeforeDateTime = startBeforeDateTime,
                endBeforeDateTime = endBeforeDateTime
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionNav between dates",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        try {
            dbRepository.deleteById(id)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "Unable to delete MissionNav='$id'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to delete MissionNav with id='$id'",
                originalException = e
            )
        }
    }
}
