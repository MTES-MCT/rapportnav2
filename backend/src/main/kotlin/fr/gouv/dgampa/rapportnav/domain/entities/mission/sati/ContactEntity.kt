package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import java.time.OffsetDateTime
import java.util.UUID

data class ContactEntity(
    val id: UUID? = null,
    val fullName: String? = null,
    val nationality: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: AddressEntity? = null,
    val createdAt: OffsetDateTime? = null
)
