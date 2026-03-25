package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import java.time.Instant
import java.util.UUID

data class AddressEntity(
    val id: UUID? = null,
    val street: String? = null,
    val zipcode: String? = null,
    val town: String? = null,
    val country: String? = null,
    val createdAt: Instant? = null
)
