package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish

import java.time.Instant

data class FishAuctionEntity(
    val id: Int? = null,
    val name: String,
    val facade: FacadeTypeEnum,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val createdBy: Int? = null,
    val updatedBy: Int? = null,
    val deletedAt: Instant? = null
)
