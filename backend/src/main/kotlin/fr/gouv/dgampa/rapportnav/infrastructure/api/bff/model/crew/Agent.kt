package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import java.time.Instant

data class Agent(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val deletedAt: Instant? = null,
) {
    companion object {
        fun fromAgentEntity(entity: AgentEntity): Agent {
            return Agent(
                id = entity.id,
                firstName = entity.firstName,
                lastName = entity.lastName,
                updatedAt = entity.updatedAt,
                createdAt = entity.createdAt,
                deletedAt = entity.deletedAt
            )
        }

        fun fromAgentServiceEntity(entity: AgentServiceEntity): Agent {
            return Agent(
                id = entity.agent.id,
                firstName = entity.agent.firstName,
                lastName = entity.agent.lastName,
                updatedAt = entity.updatedAt,
                createdAt = entity.createdAt,
                deletedAt = entity.disabledAt
            )
        }
    }

    fun toAgentEntity(): AgentEntity {
        return AgentEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            deletedAt = deletedAt
        )
    }
}
