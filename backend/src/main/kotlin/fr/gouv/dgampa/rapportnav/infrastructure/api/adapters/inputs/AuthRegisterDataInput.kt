package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.inputs

data class AuthRegisterDataInput(
    val id: Int?,
    val name: String,
    val email: String,
    val password: String,
)
