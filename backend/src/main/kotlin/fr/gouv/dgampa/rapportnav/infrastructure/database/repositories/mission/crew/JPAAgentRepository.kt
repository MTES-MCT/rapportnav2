package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAAgentRepository(private val dbAgentRepository: IDBAgentRepository) :
    IAgentRepository {

    override fun findAll(): List<AgentModel> {
        return try {
            dbAgentRepository.findAll()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRepository.findAll failed",
                originalException = e
            )
        }
    }

    override fun findAllActive(): List<AgentModel> {
        return try {
            dbAgentRepository.findAll().filter { it.disabledAt == null }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRepository.findAllActive failed",
                originalException = e
            )
        }
    }

    override fun findById(id: Int): AgentModel? {
        return try {
            dbAgentRepository.findByIdOrNull(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRepository.findById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun findByServiceId(serviceId: Int): List<AgentModel> {
        return try {
            dbAgentRepository.findByServiceId(serviceId).filter { it.disabledAt == null }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRepository.findByServiceId failed for serviceId=$serviceId",
                originalException = e
            )
        }
    }

    override fun save(model: AgentModel): AgentModel {
        return try {
            dbAgentRepository.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "JPAAgentRepository.save failed for agent id=${model.id}",
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRepository.save failed for agent id=${model.id}",
                originalException = e
            )
        }
    }

    override fun deleteById(id: Int) {
        try {
            dbAgentRepository.deleteById(id)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "JPAAgentRepository.deleteById failed for id=$id",
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRepository.deleteById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun disabledById(id: Int) {
        try {
            val agent = dbAgentRepository.findById(id).getOrNull() ?: return
            agent.disabledAt = java.time.Instant.now()
            dbAgentRepository.save(agent)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRepository.disabledById failed for id=$id",
                originalException = e
            )
        }
    }
}
