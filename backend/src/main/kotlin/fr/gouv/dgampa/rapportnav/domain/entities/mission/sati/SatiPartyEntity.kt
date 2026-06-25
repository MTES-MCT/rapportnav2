package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import java.time.Instant

data class SatiPartyEntity(
    val id: Int? = null,
    val partyType: String? = null,
    val comments: String? = null,
    val signature: Boolean = false,
    val contact: ContactEntity? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
