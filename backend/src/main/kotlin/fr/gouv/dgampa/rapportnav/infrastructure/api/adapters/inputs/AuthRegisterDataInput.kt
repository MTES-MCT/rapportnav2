package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.inputs

data class AuthRegisterDataInput(
    val id: Int?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val serviceId: Int? = null
)
