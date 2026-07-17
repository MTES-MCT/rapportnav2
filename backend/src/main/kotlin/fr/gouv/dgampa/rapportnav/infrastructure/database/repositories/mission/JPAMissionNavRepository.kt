package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.IDBMissionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
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

    override fun findById(id: UUID): Optional<MissionModel> {
        return try {
            dbRepository.findById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionNav with id='$id'",
                originalException = e
            )
        }
    }

    override fun findByExternalId(externalId: String): Optional<MissionModel> {
        return try {
            dbRepository.findByExternalId(externalId)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionNav with externalId='$externalId'",
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

    override fun findAllPaginated(page: Int, size: Int): Page<MissionModel> {
        return try {
            dbRepository.findAllByOrderByStartDateTimeUtcDesc(PageRequest.of(page, size))
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find paginated MissionNav",
                originalException = e
            )
        }
    }

    override fun findByIdPaginated(id: UUID, page: Int, size: Int): Page<MissionModel> {
        return try {
            dbRepository.findByIdOrderByStartDateTimeUtcDesc(id, PageRequest.of(page, size))
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find paginated MissionNav by id='$id'",
                originalException = e
            )
        }
    }

    override fun findByExternalIdPaginated(externalId: String, page: Int, size: Int): Page<MissionModel> {
        return try {
            dbRepository.findByExternalIdOrderByStartDateTimeUtcDesc(externalId, PageRequest.of(page, size))
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find paginated MissionNav by externalId='$externalId'",
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

    @Transactional
    override fun softDeleteById(id: UUID) {
        try {
            val model = dbRepository.findById(id).orElseThrow {
                BackendUsageException(
                    code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                    message = "Unable to soft-delete MissionNav='$id': not found",
                )
            }
            model.isDeleted = true
            dbRepository.save(model)
        } catch (e: BackendUsageException) {
            throw e
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "Unable to soft-delete MissionNav='$id'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to soft-delete MissionNav with id='$id'",
                originalException = e
            )
        }
    }
}
