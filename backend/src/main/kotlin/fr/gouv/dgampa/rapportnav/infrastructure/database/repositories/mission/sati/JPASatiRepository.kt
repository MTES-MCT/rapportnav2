package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.sati.IDBSatiRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPASatiRepository(
    private val dbRepo: IDBSatiRepository
) : ISatiRepository {

    override fun findById(id: UUID): SatiEntity? {
        return try {
            dbRepo.findById(id).orElse(null)?.let { SatiModelMapper.toEntity(it) }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPASatiRepository.findById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun findByActionId(actionId: String): SatiEntity? {
        return try {
            dbRepo.findByActionId(actionId)?.let { SatiModelMapper.toEntity(it) }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPASatiRepository.findByOwnerId failed for actionId=$actionId",
                originalException = e
            )
        }
    }

    override fun findAll(): List<SatiEntity> {
        return try {
            dbRepo.findAll().map { SatiModelMapper.toEntity(it) }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPASatiRepository.findAll failed",
                originalException = e
            )
        }
    }

    @Transactional
    override fun save(sati: SatiEntity): SatiEntity {
        return try {
            dbRepo.save(SatiModelMapper.toModel(sati)).let { SatiModelMapper.toEntity(it) }
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "JPASatiRepository.save: invalid data for sati=${sati.id}",
                e
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPASatiRepository.save failed for sati=${sati.id}",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        try {
            dbRepo.deleteById(id)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "JPASatiRepository.deleteById: invalid id=$id",
                e
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPASatiRepository.deleteById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun existsById(id: UUID): Boolean {
        return try {
            dbRepo.existsById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPASatiRepository.existsById failed for id=$id",
                originalException = e
            )
        }
    }
}
