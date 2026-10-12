package fr.gouv.dgampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RateLimitException
import org.slf4j.LoggerFactory
import java.time.Instant

/**
 * Brute-force / enumeration protection for the login endpoint (pentest finding BRUT01).
 *
 * Mirrors the API-key rate-limit pattern ([fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.ValidateApiKey]):
 * it counts recent `LOGIN_FAILURE` rows already recorded in the `authentication_audit` table and
 * temporarily blocks further attempts once a threshold is reached, within a sliding window.
 *
 * Two independent limits are enforced:
 * - per email, to stop targeted attacks against a single account;
 * - per IP, to stop account-enumeration sweeps from a single source.
 *
 * Being DB-backed, the protection is shared across application instances (no in-memory state).
 * When a limit is exceeded a [RateLimitException] is thrown, mapped to HTTP 429 by the
 * global controller exception handler.
 */
@UseCase
class CheckLoginRateLimit(
    private val auditRepository: IAuthenticationAuditRepository,
) {
    private val logger = LoggerFactory.getLogger(CheckLoginRateLimit::class.java)

    companion object {
        const val MAX_FAILURES_PER_EMAIL = 5
        const val MAX_FAILURES_PER_IP = 20
        const val WINDOW_SECONDS = 15L * 60L
    }

    /**
     * Throws [RateLimitException] if too many recent failed login attempts are recorded for the
     * given [email] or [ipAddress]. Must be called before attempting authentication.
     */
    fun execute(email: String, ipAddress: String?) {
        val since = Instant.now().minusSeconds(WINDOW_SECONDS)

        val failuresForEmail = auditRepository.countFailuresByEmailSince(email, since)
        if (failuresForEmail >= MAX_FAILURES_PER_EMAIL) {
            logger.warn(
                "Login rate limit exceeded for email={}: {} failures in last {}s",
                email, failuresForEmail, WINDOW_SECONDS
            )
            throw RateLimitException("Too many failed login attempts. Please try again later.")
        }

        if (ipAddress != null) {
            val failuresForIp = auditRepository.countFailuresByIpAddressSince(ipAddress, since)
            if (failuresForIp >= MAX_FAILURES_PER_IP) {
                logger.warn(
                    "Login rate limit exceeded for ip={}: {} failures in last {}s",
                    ipAddress, failuresForIp, WINDOW_SECONDS
                )
                throw RateLimitException("Too many failed login attempts. Please try again later.")
            }
        }
    }
}
