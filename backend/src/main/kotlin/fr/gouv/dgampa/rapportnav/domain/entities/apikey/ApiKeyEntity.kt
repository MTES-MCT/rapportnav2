package fr.gouv.dgampa.rapportnav.domain.entities.apikey

import java.time.Instant
import java.util.UUID

data class ApiKeyEntity(
    val id: UUID = UUID.randomUUID(),
    val publicId: String,
    val hashedKey: String,
    val owner: String? = null,
    val disabledAt: Instant? = null,
)
