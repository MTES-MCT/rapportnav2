package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import java.time.Instant

data class AgentEntity(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
    val deletedAt: Instant? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
){
    fun toAgentModel(): AgentModel {
        return AgentModel(
            id = id,
            firstName = firstName,
            lastName = lastName,
            deletedAt = deletedAt,
        )
    }

    companion object {
        fun fromAgentModel(agent: AgentModel): AgentEntity {
            return AgentEntity(
                id = agent.id,
                firstName = agent.firstName,
                lastName = agent.lastName,
                deletedAt = agent.deletedAt,
                createdAt = agent.createdAt,
                updatedAt = agent.updatedAt
            )
        }
    }
}
