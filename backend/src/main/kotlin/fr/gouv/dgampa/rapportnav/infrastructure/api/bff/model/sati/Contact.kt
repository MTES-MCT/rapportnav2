package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import java.time.Instant
import java.util.UUID

data class Contact(
    val id: UUID? = null,
    val fullName: String? = null,
    val nationality: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: Address? = null,
    val createdAt: Instant? = null
)
