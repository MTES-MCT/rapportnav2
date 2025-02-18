package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity

data class Agent(
    val id: Int? = null,
    val firstName: String,
    val lastName: String,
) {
    companion object {
        fun fromAgentEntity(agent: AgentEntity): Agent {
            return Agent(
                id = agent.id,
                firstName = agent.firstName,
                lastName = agent.lastName,
            )
        }
    }

    fun toAgentEntity(): AgentEntity {
        return AgentEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
        )
    }
}
