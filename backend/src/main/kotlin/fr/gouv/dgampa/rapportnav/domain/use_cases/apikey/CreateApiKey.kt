package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.SecureRandom
import java.util.*

@UseCase
class CreateApiKey(
    private val repo: IApiKeyRepository,
    private val validateApiKey: ValidateApiKey,
    private val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder(12)
) {

    private val logger = LoggerFactory.getLogger(CreateApiKey::class.java)

    val API_KEY_LENGTH = 48
    val PUBLIC_ID_LENGTH = 12

    /** Generates a new cryptographically secure API key. */
    fun generateSecureKey(): String {
        val random = SecureRandom()
        val bytes = ByteArray(API_KEY_LENGTH)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    /**
     * Creates a new API key.
     *
     * @param id Optional UUID. If invalid or null, a new one will be generated.
     * @param owner The owner identifier (optional but logged).
     * @return Pair(ApiKeyEntity, rawKey)
     * @throws IllegalStateException if key generation or persistence fails.
     */
    @Transactional
    fun execute(id: UUID?, owner: String?): Pair<ApiKeyEntity?, String> {
        try {
            val rawKey = generateSecureKey()
            require(rawKey.length >= PUBLIC_ID_LENGTH) {
                "Generated API key too short to extract public ID"
            }

            val publicId = rawKey.take(PUBLIC_ID_LENGTH)
            val hashedKey = passwordEncoder.encode(rawKey)

            val finalId = id?.takeIf { isValidUUID(it) } ?: UUID.randomUUID()

            val apiKey = ApiKeyEntity(
                id = finalId,
                publicId = publicId,
                hashedKey = hashedKey,
                owner = owner,
            )

            val saved = repo.save(ApiKeyModel.fromApiKeyEntity(apiKey))

            logger.info("✅ Created new API key for owner='{}', publicId='{}'", owner, publicId)

            return saved?.toApiKeyEntity() to rawKey
        } catch (ex: Exception) {
            logger.error("Failed to create API key for owner='{}': {}", owner, ex.message, ex)
            throw IllegalStateException("Failed to create API key", ex)
        }
    }
}
