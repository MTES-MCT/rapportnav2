package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import java.time.Instant

data class Agent(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val userId: Int? = null,
    val role: AgentRole? = null,
    val disabledAt: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val createdBy: Int? = null,
    val service: Service
) {
    fun toAgentEntity(): AgentEntity {
        return AgentEntity(
            id = id,
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            disabledAt = disabledAt,
            role = role?.toAgentRoleEntity(),
            service = service.toServiceEntity()
        )
    }

    companion object {
        fun fromAgentEntity(entity: AgentEntity?): Agent? {
            if (entity == null) return null
            return Agent(
                id = entity.id,
                userId = entity.userId,
                firstName = entity.firstName,
                lastName = entity.lastName,
                updatedAt = entity.updatedAt,
                createdAt = entity.createdAt,
                createdBy = entity.createdBy,
                disabledAt = entity.disabledAt,
                role = entity.role?.let { AgentRole.fromAgentRoleEntity(it)},
                service = entity.service.let { Service.fromServiceEntity(it) }
            )
        }
    }
}
