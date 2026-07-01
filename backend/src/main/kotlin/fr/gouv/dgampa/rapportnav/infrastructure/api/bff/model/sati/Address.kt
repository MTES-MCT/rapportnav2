package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

data class Address(
    val id: Int? = null,
    val fullAddress: String? = null,
    val street: String? = null,
    val zipcode: String? = null,
    val town: String? = null,
    val country: String? = null,
    val lat: Double? = null,
    val lng: Double? = null
)
