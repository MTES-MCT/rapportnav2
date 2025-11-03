package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRoleRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import kotlin.jvm.optionals.getOrNull

@Repository
class JPAAgentRoleRepository(
    private val dbAgentRoleRepository: IDBAgentRoleRepository
) : IAgentRoleRepository {

    override fun findById(id: Int): AgentRoleModel? {
        return dbAgentRoleRepository.findById(id).getOrNull()
    }

    override fun findAll(): List<AgentRoleModel> {
        return dbAgentRoleRepository.findAll()
    }

    override fun save(agent: AgentRoleModel): AgentRoleModel {
        return dbAgentRoleRepository.save(agent)
    }

    override fun deleteById(id: Int) {
        val agentRole = dbAgentRoleRepository.findById(id).getOrNull() ?: return
        agentRole.deletedAt = Instant.now()
        dbAgentRoleRepository.save(agentRole)
    }
}
