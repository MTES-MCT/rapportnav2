package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.models

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import java.time.Instant
import java.util.UUID

data class AdminApiKey(
    val id: UUID = UUID.randomUUID(),
    val publicId: String,
    val owner: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val lastUsedAt: Instant? = null,
    val disabledAt: Instant? = null,
) {
    companion object {
        fun fromApiKeyEntity(apiKeyEntity: ApiKeyEntity): AdminApiKey {
            return AdminApiKey(
                id = apiKeyEntity.id,
                publicId = apiKeyEntity.publicId,
                owner = apiKeyEntity.owner,
                createdAt = apiKeyEntity.createdAt,
                updatedAt = apiKeyEntity.updatedAt,
                lastUsedAt = apiKeyEntity.lastUsedAt,
                disabledAt = apiKeyEntity.disabledAt,
            )
        }
    }
}
