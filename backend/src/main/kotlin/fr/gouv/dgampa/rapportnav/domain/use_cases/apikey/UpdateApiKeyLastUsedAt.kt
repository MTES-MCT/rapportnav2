package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

@UseCase
class UpdateApiKeyLastUsedAt(
    private val repo: IApiKeyRepository
) {

    private val logger = LoggerFactory.getLogger(UpdateApiKeyLastUsedAt::class.java)

    /**
     * Updates the "lastUsedAt" timestamp of an API key.
     *
     * @param apiKey the entity to update
     * @return Optional containing the updated ApiKeyModel if found, empty if not found
     */
    @Transactional
    fun execute(apiKey: ApiKeyEntity): Optional<ApiKeyModel> {
        val now = Instant.now()

        val updated = repo.findById(apiKey.id).map { existingModel ->
            val existing = existingModel.toApiKeyEntity()
            val updatedEntity = existing.copy(lastUsedAt = now)
            val saved = repo.save(ApiKeyModel.fromApiKeyEntity(updatedEntity))

            logger.info("Updated lastUsedAt for API key '{}': {}", apiKey.publicId, now)
            saved
        }

        if (updated.isEmpty) {
            logger.warn("No API key found for id='{}' when updating lastUsedAt", apiKey.id)
        }

        return updated
    }
}
