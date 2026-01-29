package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBServiceRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAServiceRepository(
    private val repo: IDBServiceRepository
) : IServiceRepository {

    override fun existsById(id: Int): Boolean {
        return try {
            repo.existsById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAServiceRepository.existsById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun findById(id: Int): Optional<ServiceModel> {
        return try {
            repo.findById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAServiceRepository.findById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun findAll(): List<ServiceModel> {
        return try {
            repo.findAll()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAServiceRepository.findAll failed",
                originalException = e
            )
        }
    }

    override fun findByControlUnitId(controlUnitIds: List<Int>): List<ServiceModel> {
        return try {
            repo.findAll().filter { serviceModel ->
                serviceModel.controlUnits?.any { it in controlUnitIds } == true
            }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAServiceRepository.findByControlUnitId failed for controlUnitIds=$controlUnitIds",
                originalException = e
            )
        }
    }

    @Transactional
    override fun save(service: ServiceModel): ServiceModel {
        return try {
            repo.save(service)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAServiceRepository.save failed for service=${service.id}",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: Int) {
        try {
            val service = repo.findById(id).getOrNull()
                ?: throw BackendUsageException(
                    code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                    message = "JPAServiceRepository.deleteById: service not found for id=$id"
                )
            service.deletedAt = Instant.now()
            repo.save(service)
        } catch (e: BackendUsageException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAServiceRepository.deleteById failed for id=$id",
                originalException = e
            )
        }
    }
}
