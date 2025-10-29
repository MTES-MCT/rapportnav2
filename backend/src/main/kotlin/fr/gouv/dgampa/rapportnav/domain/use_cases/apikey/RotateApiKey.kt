package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory

@UseCase
class RotateApiKey(
    private val repo: IApiKeyRepository,
    private val createApiKey: CreateApiKey
) {
    private val logger = LoggerFactory.getLogger(RotateApiKey::class.java)

    /**
     * Rotates (regenerates) an API key given its public ID.
     *
     * @param publicId the public part of the key to rotate.
     * @return a pair (new ApiKeyEntity, rawKey).
     * @throws NoSuchElementException if no key exists for the given publicId.
     * @throws IllegalStateException if creation fails.
     */
    @Transactional
    fun execute(publicId: String): Pair<ApiKeyEntity?, String> {
        val existing = repo.findByPublicId(publicId)?.toApiKeyEntity()
            ?: throw NoSuchElementException("No API key found with publicId=$publicId")

        logger.info("Rotating API key for owner='{}', oldPublicId='{}'", existing.owner, publicId)

        // Create a new key with same owner, reuse same UUID (optional)
        val (newEntity, rawKey) = createApiKey.execute(existing.id, existing.owner)

        logger.info(
            "API key rotated successfully for owner='{}', newPublicId='{}'",
            existing.owner,
            newEntity?.publicId
        )

        return newEntity to rawKey
    }
}

