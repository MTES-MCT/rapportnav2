package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati

import java.time.OffsetDateTime
import java.util.UUID

data class ContactResponse(
    val id: UUID? = null,
    val fullName: String? = null,
    val nationality: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: AddressResponse? = null,
    val createdAt: OffsetDateTime? = null
)
