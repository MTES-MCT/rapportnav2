package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import java.time.Instant

data class ContactEntity(
    val id: Int? = null,
    val fullName: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val nationality: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val address: AddressEntity? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
