package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRoleRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAAgentRoleRepository(
    private val dbAgentRoleRepository: IDBAgentRoleRepository
) : IAgentRoleRepository {

    override fun findById(id: Int): AgentRoleModel? {
        return try {
            dbAgentRoleRepository.findById(id).getOrNull()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRoleRepository.findById failed for id=$id",
                originalException = e
            )
        }
    }

    override fun findAll(): List<AgentRoleModel> {
        return try {
            dbAgentRoleRepository.findAll()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRoleRepository.findAll failed",
                originalException = e
            )
        }
    }

    override fun save(agent: AgentRoleModel): AgentRoleModel {
        return try {
            dbAgentRoleRepository.save(agent)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "JPAAgentRoleRepository.save failed for role id=${agent.id}",
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRoleRepository.save failed for role id=${agent.id}",
                originalException = e
            )
        }
    }

    override fun deleteById(id: Int) {
        try {
            val agentRole = dbAgentRoleRepository.findById(id).getOrNull() ?: return
            agentRole.deletedAt = Instant.now()
            dbAgentRoleRepository.save(agentRole)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "JPAAgentRoleRepository.deleteById failed for id=$id",
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAAgentRoleRepository.deleteById failed for id=$id",
                originalException = e
            )
        }
    }
}
