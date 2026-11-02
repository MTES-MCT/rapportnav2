package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.security.MessageDigest
import java.time.Instant
import java.util.UUID

class RateLimitException(message: String) : RuntimeException(message)

@UseCase
class ValidateApiKey(
    private val apiKeyRepository: IApiKeyRepository,
    private val auditRepository: IApiKeyAuditRepository,
    private val logApiKeyAudit: LogApiKeyAudit,
    private val updateLastUsedAsync: UpdateApiKeyLastUsedAt,
    private val passwordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder(12)
) {
    private val logger = LoggerFactory.getLogger(ValidateApiKey::class.java)

    val PUBLIC_ID_LENGTH = 12
    val MAX_REQUESTS_PER_MINUTE = 60
    val MAX_REQUESTS_PER_HOUR = 1000
    val MASTER_KEY_MIN_LENGTH = 48

    /**
     * Master API key loaded from the environment.
     *
     * It is only accepted when it is present AND at least [MASTER_KEY_MIN_LENGTH] characters long.
     * A missing, blank or too-short value disables the master-key path entirely, so a weak or
     * accidental value can never enable the bypass.
     */
    private val masterKey: String? = System.getenv("MASTER_API_KEY")
        ?.takeIf { it.isNotBlank() && it.length >= MASTER_KEY_MIN_LENGTH }

    init {
        val rawMasterKey = System.getenv("MASTER_API_KEY")
        when {
            masterKey != null -> logger.info("Master API key loaded from environment variable")
            !rawMasterKey.isNullOrBlank() -> logger.warn(
                "MASTER_API_KEY is set but shorter than {} characters: it is REJECTED and the master-key path stays disabled",
                MASTER_KEY_MIN_LENGTH
            )
            else -> logger.warn("No MASTER_API_KEY environment variable found")
        }
    }

    /**
     * Validates an API key against stored values and returns the corresponding entity if valid.
     * Performs audit logging and last-used update.
     */
    @Transactional
    fun execute(apiKey: String, ipAddress: String, requestPath: String): ApiKeyEntity? {
        require(apiKey.isNotBlank()) { "API key cannot be blank." }

        // Check master key, using a constant-time comparison to avoid timing side-channels
        val currentMasterKey = masterKey
        if (currentMasterKey != null && MessageDigest.isEqual(
                apiKey.toByteArray(Charsets.UTF_8),
                currentMasterKey.toByteArray(Charsets.UTF_8)
            )
        ) {
            logger.warn("Master key used for access from ip={} path={}", ipAddress, requestPath)
            logApiKeyAudit.logSuccessfulAccess(null, ipAddress, requestPath)
            return null // Master key has no DB entity
        }

        // Validate format
        if (apiKey.length < PUBLIC_ID_LENGTH) {
            logApiKeyAudit.logFailedAccess(null, ipAddress, requestPath, "Invalid key format")
            return null
        }

        val publicId = apiKey.take(PUBLIC_ID_LENGTH)
        val storedKey = apiKeyRepository.findByPublicId(publicId)

        if (storedKey == null) {
            logApiKeyAudit.logFailedAccess(null, ipAddress, requestPath, "Key not found")
            return null
        }

        // Check hash match
        if (!passwordEncoder.matches(apiKey, storedKey.hashedKey)) {
            logApiKeyAudit.logFailedAccess(storedKey.id, ipAddress, requestPath, "Invalid key")
            return null
        }

        // Disabled key check
        if (storedKey.disabledAt != null) {
            logApiKeyAudit.logFailedAccess(storedKey.id, ipAddress, requestPath, "Key disabled")
            return null
        }

        // Rate limits (stub)
        checkRateLimits(storedKey.id)

        // Check IP-based rate limits (prevent abuse from single IP)
        checkIpRateLimits(ipAddress)

        // Successful access
        logApiKeyAudit.logSuccessfulAccess(storedKey.id, ipAddress, requestPath)
        updateLastUsedAsync.execute(storedKey.toApiKeyEntity())

        return storedKey.toApiKeyEntity()
    }


    private fun checkRateLimits(apiKeyId: UUID) {
        val now: Instant = Instant.now()

        // Check requests in last minute
        val requestsLastMinute = auditRepository.countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(
            apiKeyId,
            now.minusSeconds(60)
        )

        if (requestsLastMinute >= MAX_REQUESTS_PER_MINUTE) {
            logger.warn("Rate limit exceeded for key $apiKeyId: $requestsLastMinute requests in last minute")
            throw RateLimitException("Rate limit exceeded: $MAX_REQUESTS_PER_MINUTE requests per minute")
        }

        // Check requests in last hour
        val requestsLastHour = auditRepository.countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(
            apiKeyId,
            now.minusSeconds(3600)
        )

        if (requestsLastHour >= MAX_REQUESTS_PER_HOUR) {
            logger.warn("Rate limit exceeded for key $apiKeyId: $requestsLastHour requests in last hour")
            throw RateLimitException("Rate limit exceeded: $MAX_REQUESTS_PER_HOUR requests per hour")
        }
    }

    private  fun checkIpRateLimits(ipAddress: String) {
        val now = Instant.now()
        val requestsFromIp = auditRepository.findByIpAddressAndTimestampAfter(
            ipAddress,
            now.minusSeconds(60)
        ).size

        // Global IP rate limit (prevent DDoS)
        if (requestsFromIp > MAX_REQUESTS_PER_MINUTE) {
            logger.warn("IP $ipAddress exceeded global rate limit")
            throw RateLimitException("Too many requests from your IP address")
        }
    }

}
