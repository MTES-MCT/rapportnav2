package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import java.time.Instant

data class AgentRole(
    val id: Int?,
    val title: String,
    val priority: Int? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
) {
    fun toAgentRoleEntity(): AgentRoleEntity {
        return AgentRoleEntity(
            id = id,
            title = title,
            priority = priority
        )
    }

    companion object {
        fun fromAgentRoleEntity(role: AgentRoleEntity): AgentRole {
            return AgentRole(
                id = role.id,
                title = role.title,
                priority = role.priority,
                createdAt = role.createdAt,
                updatedAt = role.updatedAt
            )
        }
    }
}
