package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64
import java.util.UUID

class RateLimitException(message: String) : RuntimeException(message)

@Service
class ApiKeyService(
    private val apiKeyRepository: IApiKeyRepository,
    private val auditRepository: IApiKeyAuditRepository,
    private val updateLastUsedAsync: UpdateApiKeyLastUsedAt,
    private val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder(12)
) {
    private val logger = LoggerFactory.getLogger(ApiKeyService::class.java)

    private val masterKeyFromEnv = System.getenv("MASTER_API_KEY")

    init {
        if (masterKeyFromEnv != null) {
            logger.info("Master API key loaded from environment variable")
        } else {
            logger.warn("No MASTER_API_KEY environment variable found")
        }
    }

    val API_KEY_LENGTH = 48
    val PUBLIC_ID_LENGTH = 12
    val MAX_REQUESTS_PER_MINUTE = 60
    val MAX_REQUESTS_PER_HOUR = 1000

    fun generateSecureKey(): String {
        val random = SecureRandom()
        val bytes = ByteArray(API_KEY_LENGTH)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    /**
     * Validates API key using the same mechanism:
     * 1. Lookup by publicId (first 12 chars)
     * 2. Verify full key against BCrypt hash
     */
    @Transactional
    fun validateApiKey(
        apiKey: String,
        ipAddress: String,
        requestPath: String
    ): ApiKeyEntity? {
        // First, check if it's the master key from environment
        if (masterKeyFromEnv != null && apiKey == masterKeyFromEnv) {
            logger.debug("Master key from environment validated")
            logSuccessfulAccess(null, ipAddress, requestPath) // null = master key
        }

        // Extract public ID (first 12 chars)
        if (apiKey.length < PUBLIC_ID_LENGTH) {
            logFailedAccess(null, ipAddress, requestPath, "Invalid key format")
            return null
        }

        val publicId = apiKey.take(PUBLIC_ID_LENGTH)

        // Lookup by public ID
        val storedKey = apiKeyRepository.findByPublicId(publicId)

        if (storedKey == null) {
            logFailedAccess(null, ipAddress, requestPath, "Key not found")
            return null
        }

        // Verify full key against BCrypt hash (constant-time comparison)
        if (!passwordEncoder.matches(apiKey, storedKey.hashedKey)) {
            logFailedAccess(storedKey.id, ipAddress, requestPath, "Invalid key")
            return null
        }

        // Check if disabled
        if (storedKey.disabledAt != null) {
            logFailedAccess(storedKey.id, ipAddress, requestPath, "Key disabled")
            return null
        }

        // Check rate limits
        checkRateLimits(storedKey.id)

        // Log successful access
        logSuccessfulAccess(storedKey.id, ipAddress, requestPath)

        // Update last used timestamp async
        updateLastUsedAsync.execute(storedKey.toApiKeyEntity())

        return storedKey.toApiKeyEntity()
    }


    private fun checkRateLimits(apiKeyId: UUID) {
//        val now: Instant = Instant.now()
//
//        // Check requests in last minute
//        val requestsLastMinute = auditRepository.countSuccessfulRequestsSince(
//            apiKeyId,
//            now.minusSeconds(60)
//        )
//
//        if (requestsLastMinute >= MAX_REQUESTS_PER_MINUTE) {
//            logger.warn("Rate limit exceeded for key $apiKeyId: $requestsLastMinute requests in last minute")
//            throw RateLimitException("Rate limit exceeded: $MAX_REQUESTS_PER_MINUTE requests per minute")
//        }
//
//        // Check requests in last hour
//        val requestsLastHour = auditRepository.countSuccessfulRequestsSince(
//            apiKeyId,
//            now.minusSeconds(3600)
//        )
//
//        if (requestsLastHour >= MAX_REQUESTS_PER_HOUR) {
//            logger.warn("Rate limit exceeded for key $apiKeyId: $requestsLastHour requests in last hour")
//            throw RateLimitException("Rate limit exceeded: $MAX_REQUESTS_PER_HOUR requests per hour")
//        }
    }

    fun getAuditLogs(apiKeyId: UUID, since: Instant): List<ApiKeyAuditModel> {
        return auditRepository.findByApiKeyIdAndTimestampAfter(apiKeyId, since)
    }

    private fun logSuccessfulAccess(apiKeyId: UUID?, ipAddress: String, requestPath: String) {
        auditRepository.save(
            ApiKeyAuditModel(
                apiKeyId = apiKeyId,
                ipAddress = ipAddress,
                requestPath = requestPath,
                success = true
            )
        )
    }

    private fun logFailedAccess(
        apiKeyId: UUID?,
        ipAddress: String,
        requestPath: String,
        reason: String
    ) {
        auditRepository.save(
            ApiKeyAuditModel(
                apiKeyId = apiKeyId,
                ipAddress = ipAddress,
                requestPath = requestPath,
                success = false,
                failureReason = reason
            )
        )
    }
}
