package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import java.time.OffsetDateTime
import java.util.UUID

data class AddressResponse(
    val id: UUID? = null,
    val street: String? = null,
    val zipcode: String? = null,
    val town: String? = null,
    val country: String? = null,
    val createdAt: OffsetDateTime? = null
)
