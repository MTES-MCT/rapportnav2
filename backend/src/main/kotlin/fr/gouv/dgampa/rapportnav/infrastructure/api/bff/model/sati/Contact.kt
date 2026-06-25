package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

data class Contact(
    val id: Int? = null,
    val fullName: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val nationality: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: Address? = null
)
