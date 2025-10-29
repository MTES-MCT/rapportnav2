package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class LogApiKeyAudit(
    private val auditRepository: IApiKeyAuditRepository,
) {

    private val logger = LoggerFactory.getLogger(LogApiKeyAudit::class.java)

    /**
     * Logs a successful API key access.
     */
    fun logSuccessfulAccess(apiKeyId: UUID?, ipAddress: String, requestPath: String): ApiKeyAuditModel {
        val model = buildAuditModel(
            apiKeyId,
            ipAddress,
            requestPath,
            success = true
        )
        val saved = auditRepository.save(model)

        logger.info("✅ Successful API key access: keyId={}, ip={}, path={}", apiKeyId, ipAddress, requestPath)
        return saved
    }

    /**
     * Logs a failed API key access attempt.
     */
    fun logFailedAccess(
        apiKeyId: UUID?,
        ipAddress: String,
        requestPath: String,
        reason: String
    ): ApiKeyAuditModel {
        val model = buildAuditModel(
            apiKeyId,
            ipAddress,
            requestPath,
            success = false,
            failureReason = reason
        )
        val saved = auditRepository.save(model)

        logger.warn(
            "⚠️ Failed API key access: keyId={}, ip={}, path={}, reason={}",
            apiKeyId,
            ipAddress,
            requestPath,
            reason
        )
        return saved
    }

    private fun buildAuditModel(
        apiKeyId: UUID?,
        ipAddress: String,
        requestPath: String,
        success: Boolean,
        failureReason: String? = null
    ): ApiKeyAuditModel {
        require(ipAddress.isNotBlank()) { "IP address must not be blank" }
        require(requestPath.isNotBlank()) { "Request path must not be blank" }

        return ApiKeyAuditModel(
            apiKeyId = apiKeyId,
            ipAddress = ipAddress,
            requestPath = requestPath,
            success = success,
            failureReason = failureReason,
        )
    }
}

