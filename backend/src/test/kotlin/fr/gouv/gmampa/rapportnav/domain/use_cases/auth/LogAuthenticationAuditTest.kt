package fr.gouv.gmampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthEventTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.LogAuthenticationAudit
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [LogAuthenticationAudit::class])
@ContextConfiguration(classes = [LogAuthenticationAudit::class])
class LogAuthenticationAuditTest {

    @Autowired
    private lateinit var logAuthenticationAudit: LogAuthenticationAudit

    @MockitoBean
    private lateinit var repo: IAuthenticationAuditRepository

    @BeforeEach
    fun setUp() {
        repo = mock()
        logAuthenticationAudit = LogAuthenticationAudit(repo)
    }

    @Test
    fun `should log successful login`() {
        // given
        val userId = 1
        val email = "user@example.com"
        val ip = "192.168.1.10"
        val userAgent = "Mozilla/5.0"
        val expectedModel = AuthenticationAuditModel(
            userId = userId,
            email = email,
            eventType = AuthEventTypeEnum.LOGIN_SUCCESS,
            ipAddress = ip,
            userAgent = userAgent,
            success = true,
        )

        whenever(repo.save(any())).thenReturn(expectedModel)

        // when
        val result = logAuthenticationAudit.logLoginSuccess(userId, email, ip, userAgent)

        // then
        assertTrue(result.success)
        assertEquals(email, result.email)
        assertEquals(AuthEventTypeEnum.LOGIN_SUCCESS, result.eventType)
        verify(repo).save(any())
    }

    @Test
    fun `should log failed login with reason`() {
        // given
        val email = "user@example.com"
        val ip = "10.0.0.1"
        val userAgent = "Mozilla/5.0"
        val reason = "Invalid password"
        val expectedModel = AuthenticationAuditModel(
            userId = null,
            email = email,
            eventType = AuthEventTypeEnum.LOGIN_FAILURE,
            ipAddress = ip,
            userAgent = userAgent,
            success = false,
            failureReason = reason,
        )

        whenever(repo.save(any())).thenReturn(expectedModel)

        // when
        val result = logAuthenticationAudit.logLoginFailure(email, ip, userAgent, reason)

        // then
        assertFalse(result.success)
        assertEquals(reason, result.failureReason)
        assertEquals(AuthEventTypeEnum.LOGIN_FAILURE, result.eventType)
        verify(repo).save(any())
    }

    @Test
    fun `should log logout`() {
        // given
        val userId = 1
        val email = "user@example.com"
        val ip = "192.168.1.10"
        val userAgent = "Mozilla/5.0"
        val expectedModel = AuthenticationAuditModel(
            userId = userId,
            email = email,
            eventType = AuthEventTypeEnum.LOGOUT,
            ipAddress = ip,
            userAgent = userAgent,
            success = true,
        )

        whenever(repo.save(any())).thenReturn(expectedModel)

        // when
        val result = logAuthenticationAudit.logLogout(userId, email, ip, userAgent)

        // then
        assertTrue(result.success)
        assertEquals(AuthEventTypeEnum.LOGOUT, result.eventType)
        verify(repo).save(any())
    }

    @Test
    fun `should throw if email is blank`() {
        assertThrows<IllegalArgumentException> {
            logAuthenticationAudit.logLoginSuccess(1, "", "127.0.0.1", "Test-Agent")
        }
        verify(repo, never()).save(any())
    }

    @Test
    fun `should handle null ip and user agent`() {
        // given
        val userId = 1
        val email = "user@example.com"
        val expectedModel = AuthenticationAuditModel(
            userId = userId,
            email = email,
            eventType = AuthEventTypeEnum.LOGIN_SUCCESS,
            ipAddress = null,
            userAgent = null,
            success = true,
        )

        whenever(repo.save(any())).thenReturn(expectedModel)

        // when
        val result = logAuthenticationAudit.logLoginSuccess(userId, email, null, null)

        // then
        assertTrue(result.success)
        assertNull(result.ipAddress)
        assertNull(result.userAgent)
        verify(repo).save(any())
    }

    @Test
    fun `should propagate exception if save fails`() {
        // given
        whenever(repo.save(any())).thenThrow(RuntimeException("DB error"))

        // when + then
        val ex = assertThrows<RuntimeException> {
            logAuthenticationAudit.logLoginSuccess(1, "user@example.com", "1.1.1.1", "Test-Agent")
        }
        assertEquals("DB error", ex.message)
    }
}
