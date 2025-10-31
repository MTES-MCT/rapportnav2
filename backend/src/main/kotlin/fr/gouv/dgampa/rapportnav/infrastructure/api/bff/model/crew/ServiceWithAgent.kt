package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

data class ServiceWithAgent(
    val service: Service,
    val agentServices: List<AgentService>
)
