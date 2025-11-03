package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
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
        return repo.existsById(id)
    }

    override fun findById(id: Int): Optional<ServiceModel> {
        return repo.findById(id)
    }

    override fun findAll(): List<ServiceModel> {
        return repo.findAll()
    }

    override fun findByControlUnitId(controlUnitIds: List<Int>): List<ServiceModel> {
        return repo.findAll().filter { serviceModel ->
            serviceModel.controlUnits?.any { it in controlUnitIds } == true
        }
    }

    @Transactional
    override fun save(service: ServiceModel): ServiceModel {
        return try {
            repo.save(service)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: Int) {
        val service = repo.findById(id).getOrNull() ?: return
        service.deletedAt = Instant.now()
        repo.save(service)
    }
}
