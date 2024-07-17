package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity

data class AgentRole(
    val id: Int?,
    val title: String
) {
    fun toAgentRoleEntity(): AgentRoleEntity {
        return AgentRoleEntity(
            id = id,
            title = title
        )
    }

    companion object {
        fun fromAgentRoleEntity(role: AgentRoleEntity): AgentRole {
            return AgentRole(
                id = role.id,
                title = role.title,
            )
        }
    }
}
