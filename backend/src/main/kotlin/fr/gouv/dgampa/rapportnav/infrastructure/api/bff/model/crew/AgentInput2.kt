package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew

data class AgentInput2(
    var id: Int? = null,
    val firstName: String,
    val lastName: String,
    val userId: Int? = null,
    val roleId: Int? = null,
    var serviceId: Int
)
