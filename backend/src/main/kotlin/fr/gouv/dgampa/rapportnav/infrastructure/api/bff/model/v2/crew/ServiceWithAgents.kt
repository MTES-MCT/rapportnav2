package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity

data class ServiceWithAgents(
    val service: ServiceEntity,
    val agents: List<AgentServiceEntity>
)
