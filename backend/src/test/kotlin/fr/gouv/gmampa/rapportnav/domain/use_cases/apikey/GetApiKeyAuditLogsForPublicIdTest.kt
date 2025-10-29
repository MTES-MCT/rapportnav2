package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.GetApiKeyAuditLogsForPublicId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetApiKeyAuditLogsForPublicId::class])
@ContextConfiguration(classes = [GetApiKeyAuditLogsForPublicId::class])
class GetApiKeyAuditLogsForPublicIdTest {

    @Autowired
    private lateinit var getApiKeyAuditLogs: GetApiKeyAuditLogsForPublicId

    @MockitoBean
    private lateinit var repo: IApiKeyAuditRepository

    @BeforeEach
    fun setUp() {
        repo = mock()
        getApiKeyAuditLogs = GetApiKeyAuditLogsForPublicId(repo)
    }

    @Test
    fun `should return audit logs for valid apiKeyId`() {
        // given
        val apiKeyId = UUID.randomUUID()
        val since = Instant.now().minusSeconds(3600)
        val logs = listOf(
            ApiKeyAuditModel(apiKeyId = apiKeyId, ipAddress = "1.1.1.1", requestPath = "/api/test", success = true),
            ApiKeyAuditModel(apiKeyId = apiKeyId, ipAddress = "2.2.2.2", requestPath = "/api/other", success = false)
        )

        whenever(repo.findByApiKeyIdAndTimestampAfter(apiKeyId, since)).thenReturn(logs)

        // when
        val result = getApiKeyAuditLogs.execute(apiKeyId, since)

        // then
        assertEquals(2, result.size)
        assertTrue(result.any { !it.success })
        verify(repo).findByApiKeyIdAndTimestampAfter(apiKeyId, since)
    }

    @Test
    fun `should return empty list when no logs found`() {
        // given
        val apiKeyId = UUID.randomUUID()
        val since = Instant.now().minusSeconds(7200)
        whenever(repo.findByApiKeyIdAndTimestampAfter(apiKeyId, since)).thenReturn(emptyList())

        // when
        val result = getApiKeyAuditLogs.execute(apiKeyId, since)

        // then
        assertTrue(result.isEmpty())
        verify(repo).findByApiKeyIdAndTimestampAfter(apiKeyId, since)
    }

    @Test
    fun `should throw if since is in the future`() {
        // given
        val apiKeyId = UUID.randomUUID()
        val since = Instant.now().plusSeconds(60)

        // when + then
        val ex = assertThrows<IllegalArgumentException> {
            getApiKeyAuditLogs.execute(apiKeyId, since)
        }
        assertEquals("The 'since' parameter cannot be in the future.", ex.message)
        verify(repo, never()).findByApiKeyIdAndTimestampAfter(any(), any())
    }

    @Test
    fun `should propagate exception if repository throws`() {
        // given
        val apiKeyId = UUID.randomUUID()
        val since = Instant.now().minusSeconds(60)
        whenever(repo.findByApiKeyIdAndTimestampAfter(any(), any())).thenThrow(RuntimeException("DB error"))

        // when + then
        val ex = assertThrows<RuntimeException> {
            getApiKeyAuditLogs.execute(apiKeyId, since)
        }

        assertEquals("DB error", ex.message)
    }
}
