package fr.gouv.dgampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthEventTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import org.slf4j.LoggerFactory

@UseCase
class LogAuthenticationAudit(
    private val auditRepository: IAuthenticationAuditRepository,
) {

    private val logger = LoggerFactory.getLogger(LogAuthenticationAudit::class.java)

    /**
     * Logs a successful login.
     */
    fun logLoginSuccess(
        userId: Int,
        email: String,
        ipAddress: String?,
        userAgent: String?
    ): AuthenticationAuditModel {
        val model = buildAuditModel(
            userId = userId,
            email = email,
            eventType = AuthEventTypeEnum.LOGIN_SUCCESS,
            ipAddress = ipAddress,
            userAgent = userAgent,
            success = true
        )
        val saved = auditRepository.save(model)

        logger.info("Login success: userId={}, email={}, ip={}", userId, email, ipAddress)
        return saved
    }

    /**
     * Logs a failed login attempt.
     */
    fun logLoginFailure(
        email: String,
        ipAddress: String?,
        userAgent: String?,
        reason: String
    ): AuthenticationAuditModel {
        val model = buildAuditModel(
            userId = null,
            email = email,
            eventType = AuthEventTypeEnum.LOGIN_FAILURE,
            ipAddress = ipAddress,
            userAgent = userAgent,
            success = false,
            failureReason = reason
        )
        val saved = auditRepository.save(model)

        logger.warn("Login failure: email={}, ip={}, reason={}", email, ipAddress, reason)
        return saved
    }

    /**
     * Logs a logout event.
     */
    fun logLogout(
        userId: Int,
        email: String,
        ipAddress: String?,
        userAgent: String?
    ): AuthenticationAuditModel {
        val model = buildAuditModel(
            userId = userId,
            email = email,
            eventType = AuthEventTypeEnum.LOGOUT,
            ipAddress = ipAddress,
            userAgent = userAgent,
            success = true
        )
        val saved = auditRepository.save(model)

        logger.info("Logout: userId={}, email={}, ip={}", userId, email, ipAddress)
        return saved
    }

    /**
     * Logs a token refresh event.
     */
    fun logTokenRefresh(
        userId: Int,
        email: String,
        ipAddress: String?,
        userAgent: String?
    ): AuthenticationAuditModel {
        val model = buildAuditModel(
            userId = userId,
            email = email,
            eventType = AuthEventTypeEnum.TOKEN_REFRESH,
            ipAddress = ipAddress,
            userAgent = userAgent,
            success = true
        )
        val saved = auditRepository.save(model)

        logger.info("Token refresh: userId={}, email={}, ip={}", userId, email, ipAddress)
        return saved
    }

    private fun buildAuditModel(
        userId: Int?,
        email: String,
        eventType: AuthEventTypeEnum,
        ipAddress: String?,
        userAgent: String?,
        success: Boolean,
        failureReason: String? = null
    ): AuthenticationAuditModel {
        require(email.isNotBlank()) { "Email must not be blank" }

        return AuthenticationAuditModel(
            userId = userId,
            email = email,
            eventType = eventType,
            ipAddress = ipAddress,
            userAgent = userAgent?.take(500),
            success = success,
            failureReason = failureReason
        )
    }
}