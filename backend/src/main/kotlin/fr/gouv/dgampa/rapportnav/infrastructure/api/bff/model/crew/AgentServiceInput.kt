package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

data class AgentServiceInput(
    val id: Int? = null,
    val agentId: Int,
    val roleId: Int? = null,
    val serviceId: Int
)
