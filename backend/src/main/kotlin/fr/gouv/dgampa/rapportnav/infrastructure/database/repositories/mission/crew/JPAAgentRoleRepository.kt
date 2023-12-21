package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRoleRepository
import org.springframework.stereotype.Repository

@Repository
class JPAAgentRoleRepository(
    private val dbAgentRoleRepository: IDBAgentRoleRepository,
    private val mapper: ObjectMapper
) : IAgentRoleRepository {
    override fun findAll(): List<AgentRoleModel> {
        return dbAgentRoleRepository.findAll()
    }


}
