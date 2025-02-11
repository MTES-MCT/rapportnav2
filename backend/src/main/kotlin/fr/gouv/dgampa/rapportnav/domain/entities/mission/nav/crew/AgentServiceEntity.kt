package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

data class AgentServiceEntity(
    val id: Int? = null,
    val agent: AgentEntity,
    val role: AgentRoleEntity,
)
