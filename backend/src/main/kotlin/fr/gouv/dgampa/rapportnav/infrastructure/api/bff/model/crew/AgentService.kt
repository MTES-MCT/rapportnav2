package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import java.time.Instant

data class AgentService(
    val id: Int? = null,
    val agent: Agent,
    val role: AgentRole? = null,
    val disabledAt: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val createdBy: Int? = null,
    val serviceId: Int? = null
) {
    companion object {

        fun fromAgentServiceEntity(entity: AgentServiceEntity): AgentService {
            return AgentService(
                id = entity.id,
                serviceId = entity.service.id,
                agent = Agent.fromAgentEntity(entity.agent),
                role = entity.role?.let { AgentRole.fromAgentRoleEntity(it) },
                updatedAt = entity.updatedAt,
                createdAt = entity.createdAt,
                disabledAt = entity.disabledAt
            )
        }
    }
}
