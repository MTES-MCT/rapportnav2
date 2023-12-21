package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity

data class AgentRoleInput(
    val id: Int?,
    val title: String
) {
    fun toAgentRoleEntity(): AgentRoleEntity {
        return AgentRoleEntity(
            id = id,
            title = title
        )
    }
}
