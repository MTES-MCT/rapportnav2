package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.SecureRandom
import java.util.Base64

@UseCase
class CreateApiKey(
    private val repo: IApiKeyRepository,
    private val apiKeyService: ApiKeyService
) {
    private val logger = LoggerFactory.getLogger(CreateApiKey::class.java)

    fun generateSecureKey(): String {
        val random = SecureRandom()
        val bytes = ByteArray(apiKeyService.API_KEY_LENGTH)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    /**
     * Creates a new API key
     * Returns: Pair(ApiKey entity, raw key string)
     * The raw key is only returned once and should be stored securely by the user
     */
    @Transactional
    fun execute(owner: String?): Pair<ApiKeyEntity, String> {
        val passwordEncoder = BCryptPasswordEncoder(12)

        // Generate secure random key
        val rawKey = apiKeyService.generateSecureKey()

        // Extract public ID (first 12 chars for lookup)
        val publicId = rawKey.take(apiKeyService.PUBLIC_ID_LENGTH)

        // Hash the full key
        val hashedKey = passwordEncoder.encode(rawKey)

        val apiKey = ApiKeyEntity(
            publicId = publicId,
            hashedKey = hashedKey,
            owner = owner,
        )

        val saved = repo.save(apiKey)

        logger.info("Created new API key for owner: $owner")

        // Return both entity and raw key (raw key shown only once!)
        return Pair(saved.toApiKeyEntity(), rawKey)
    }
}
