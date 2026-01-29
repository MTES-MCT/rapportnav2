package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgent2Repository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAAgent2Repository(private val dbAgent2Repository: IDBAgent2Repository) :
    IAgent2Repository {

    override fun findAll(): List<AgentModel2> {
        return try {
            dbAgent2Repository.findAll()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgent2Repository.findAll failed",
                originalException = e
            )
        }
    }

    override fun findAllActive(): List<AgentModel2> {
        return try {
            dbAgent2Repository.findAll().filter { it.disabledAt == null }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgent2Repository.findAllActive failed",
                originalException = e
            )
        }
    }

    override fun findById(id: Int): AgentModel2? {
        return try {
            dbAgent2Repository.findByIdOrNull(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgent2Repository.findById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun findByServiceId(serviceId: Int): List<AgentModel2> {
        return try {
            dbAgent2Repository.findByServiceId(serviceId).filter { it.disabledAt == null }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgent2Repository.findByServiceId failed for serviceId=$serviceId",
                originalException = e
            )
        }
    }

    override fun save(model: AgentModel2): AgentModel2 {
        return try {
            dbAgent2Repository.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "JPAAgent2Repository.save failed for agent id=${model.id}",
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgent2Repository.save failed for agent id=${model.id}",
                originalException = e
            )
        }
    }

    override fun deleteById(id: Int) {
        try {
            dbAgent2Repository.deleteById(id)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "JPAAgent2Repository.deleteById failed for id=$id",
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgent2Repository.deleteById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun disabledById(id: Int) {
        try {
            val agent = dbAgent2Repository.findById(id).getOrNull() ?: return
            dbAgent2Repository.save(agent)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgent2Repository.disabledById failed for id=$id",
                originalException = e
            )
        }
    }
}
