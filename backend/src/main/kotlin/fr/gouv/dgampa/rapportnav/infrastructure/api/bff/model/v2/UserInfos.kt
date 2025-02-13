package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

data class UserInfos(
    val id: Int,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val serviceId: Int? = null,
    val serviceName: String? = null
)
