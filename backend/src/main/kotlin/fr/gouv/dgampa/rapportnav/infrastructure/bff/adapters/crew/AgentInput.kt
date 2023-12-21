package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity

data class AgentInput(
    val id: Int?,
    val firstName: String,
    val lastName: String,
) {
    fun toAgentEntity(): AgentEntity {
        return AgentEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
        )
    }
}
