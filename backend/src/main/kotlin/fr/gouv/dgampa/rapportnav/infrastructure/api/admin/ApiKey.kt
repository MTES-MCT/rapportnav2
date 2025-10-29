package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import java.time.Instant
import java.util.*

data class ApiKey(
    val id: UUID = UUID.randomUUID(),
    val publicId: String,
    val owner: String? = null,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val lastUsedAt: Instant? = null,
    val disabledAt: Instant? = null,
) {
    companion object {
        fun fromApiKeyEntity(apiKeyEntity: ApiKeyEntity): ApiKey {
            return ApiKey(
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
