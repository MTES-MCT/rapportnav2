package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.LogApiKeyAudit
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.Mockito.verify
import org.mockito.kotlin.never
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [LogApiKeyAudit::class])
@ContextConfiguration(classes = [LogApiKeyAudit::class])
class LogApiKeyAuditTest {

    @Autowired
    private lateinit var logApiKeyAudit: LogApiKeyAudit

    @MockitoBean
    private lateinit var repo: IApiKeyAuditRepository

    @BeforeEach
    fun setUp() {
        repo = mock()
        logApiKeyAudit = LogApiKeyAudit(repo)
    }

    @Test
    fun `should log successful access`() {
        // given
        val keyId = UUID.randomUUID()
        val ip = "192.168.1.10"
        val path = "/api/test"
        val expectedModel = ApiKeyAuditModel(
            apiKeyId = keyId,
            ipAddress = ip,
            requestPath = path,
            success = true,
        )

        whenever(repo.save(any())).thenReturn(expectedModel)

        // when
        val result = logApiKeyAudit.logSuccessfulAccess(keyId, ip, path)

        // then
        assertTrue(result.success)
        assertEquals(ip, result.ipAddress)
        assertEquals(path, result.requestPath)
        verify(repo).save(any())
    }

    @Test
    fun `should log failed access with reason`() {
        // given
        val keyId = UUID.randomUUID()
        val ip = "10.0.0.1"
        val path = "/api/secure"
        val reason = "Invalid signature"
        val expectedModel = ApiKeyAuditModel(
            apiKeyId = keyId,
            ipAddress = ip,
            requestPath = path,
            success = false,
            failureReason = reason,
        )

        whenever(repo.save(any())).thenReturn(expectedModel)

        // when
        val result = logApiKeyAudit.logFailedAccess(keyId, ip, path, reason)

        // then
        assertFalse(result.success)
        assertEquals(reason, result.failureReason)
        verify(repo).save(any())
    }

    @Test
    fun `should throw if ip is blank`() {
        assertThrows<IllegalArgumentException> {
            logApiKeyAudit.logSuccessfulAccess(UUID.randomUUID(), "", "/api/test")
        }
        verify(repo, never()).save(any())
    }

    @Test
    fun `should throw if request path is blank`() {
        assertThrows<IllegalArgumentException> {
            logApiKeyAudit.logFailedAccess(UUID.randomUUID(), "127.0.0.1", "", "Missing path")
        }
        verify(repo, never()).save(any())
    }

    @Test
    fun `should propagate exception if save fails`() {
        // given
        whenever(repo.save(any())).thenThrow(RuntimeException("DB error"))

        // when + then
        val ex = assertThrows<RuntimeException> {
            logApiKeyAudit.logSuccessfulAccess(UUID.randomUUID(), "1.1.1.1", "/api/test")
        }
        assertEquals("DB error", ex.message)
    }
}
