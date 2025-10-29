package fr.gouv.dgampa.rapportnav.domain.entities.apikey

import java.time.Instant
import java.util.UUID

data class ApiKeyEntity(
    val id: UUID = UUID.randomUUID(),
    val publicId: String,
    val hashedKey: String,
    val owner: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val lastUsedAt: Instant? = null,
    val disabledAt: Instant? = null,
)
