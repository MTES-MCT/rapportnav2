package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew

import java.time.Instant

data class AgentServiceEntity(
    val id: Int? = null,
    val agent: AgentEntity,
    val role: AgentRoleEntity? = null,
    val disabledAt: Instant? = null
)
