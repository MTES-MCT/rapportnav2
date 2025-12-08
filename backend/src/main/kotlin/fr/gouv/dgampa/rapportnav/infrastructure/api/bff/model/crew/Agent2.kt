package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import java.time.Instant

data class Agent2(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val userId: Int? = null,
    val role: AgentRole? = null,
    val disabledAt: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val createdBy: Int? = null,
    val service: ServiceEntity
) {
    companion object {
        fun fromAgentEntity(entity: AgentEntity2?): Agent2? {
            if (entity == null) return null
            return Agent2(
                id = entity.id,
                userId = entity.userId,
                service = entity.service,
                firstName = entity.firstName,
                lastName = entity.lastName,
                updatedAt = entity.updatedAt,
                createdAt = entity.createdAt,
                createdBy = entity.createdBy,
                disabledAt = entity.disabledAt,
                role = entity.role?.let { AgentRole.fromAgentRoleEntity(it)}
            )
        }
    }
}
