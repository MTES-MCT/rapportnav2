package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import java.time.Instant
import java.util.UUID

data class Address(
    val id: UUID? = null,
    val street: String? = null,
    val zipcode: String? = null,
    val town: String? = null,
    val country: String? = null,
    val createdAt: Instant? = null
)
