package fr.gouv.gmampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.repositories.user.IAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RateLimitException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.CheckLoginRateLimit
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant

class CheckLoginRateLimitTest {

    private lateinit var repo: IAuthenticationAuditRepository
    private lateinit var checkLoginRateLimit: CheckLoginRateLimit

    private val email = "user@example.com"
    private val ip = "10.0.0.1"

    @BeforeEach
    fun setUp() {
        repo = mock()
        checkLoginRateLimit = CheckLoginRateLimit(repo)
    }

    @Test
    fun `does not throw when under both thresholds`() {
        whenever(repo.countFailuresByEmailSince(eq(email), any())).thenReturn(4L)
        whenever(repo.countFailuresByIpAddressSince(eq(ip), any())).thenReturn(19L)

        assertDoesNotThrow { checkLoginRateLimit.execute(email, ip) }
    }

    @Test
    fun `throws when email failures reach the threshold`() {
        whenever(repo.countFailuresByEmailSince(eq(email), any()))
            .thenReturn(CheckLoginRateLimit.MAX_FAILURES_PER_EMAIL.toLong())

        assertThrows<RateLimitException> { checkLoginRateLimit.execute(email, ip) }
    }

    @Test
    fun `throws when ip failures reach the threshold`() {
        whenever(repo.countFailuresByEmailSince(eq(email), any())).thenReturn(0L)
        whenever(repo.countFailuresByIpAddressSince(eq(ip), any()))
            .thenReturn(CheckLoginRateLimit.MAX_FAILURES_PER_IP.toLong())

        assertThrows<RateLimitException> { checkLoginRateLimit.execute(email, ip) }
    }

    @Test
    fun `does not query ip when ip is null and email under threshold`() {
        whenever(repo.countFailuresByEmailSince(eq(email), any())).thenReturn(0L)

        assertDoesNotThrow { checkLoginRateLimit.execute(email, null) }
    }

    @Test
    fun `uses a sliding window strictly in the past`() {
        whenever(repo.countFailuresByEmailSince(eq(email), any())).thenReturn(0L)
        whenever(repo.countFailuresByIpAddressSince(eq(ip), any())).thenReturn(0L)

        checkLoginRateLimit.execute(email, ip)

        // The window start passed to the repository must be in the past.
        val captor = org.mockito.kotlin.argumentCaptor<Instant>()
        org.mockito.kotlin.verify(repo).countFailuresByEmailSince(eq(email), captor.capture())
        assert(captor.firstValue.isBefore(Instant.now()))
    }
}
