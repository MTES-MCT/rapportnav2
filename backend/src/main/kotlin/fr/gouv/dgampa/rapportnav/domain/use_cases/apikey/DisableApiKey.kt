package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import java.time.Instant

@UseCase
class DisableApiKey(
    private val repo: IApiKeyRepository
) {

    private val logger = LoggerFactory.getLogger(DisableApiKey::class.java)

    /**
     * Disables an API key identified by its public ID.
     *
     * @param publicId the public key identifier
     * @return true if the key was found and disabled, false if not found or already disabled
     */
    @Transactional
    fun execute(publicId: String): Boolean {
        val existing = repo.findByPublicId(publicId)?.toApiKeyEntity()

        if (existing == null) {
            logger.warn("No API key found for publicId='{}'", publicId)
            return false
        }

        if (existing.disabledAt != null) {
            logger.info("API key already disabled: publicId='{}'", publicId)
            return false
        }

        val now = Instant.now()

        return try {
            repo.save(ApiKeyModel.fromApiKeyEntity(existing.copy(disabledAt = now)))
            logger.info("Disabled API key: publicId='{}' at {}", publicId, now)
            true
        } catch (ex: Exception) {
            logger.error("Failed to disable API key '{}': {}", publicId, ex.message, ex)
            false
        }
    }
}
