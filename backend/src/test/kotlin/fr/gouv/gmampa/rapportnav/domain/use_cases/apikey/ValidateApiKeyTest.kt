package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.LogApiKeyAudit
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RateLimitException
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.UpdateApiKeyLastUsedAt
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.ValidateApiKey
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [ValidateApiKey::class])
@ContextConfiguration(classes = [ValidateApiKey::class])
class ValidateApiKeyTest {

    @Autowired
    private lateinit var validateApiKey: ValidateApiKey

    @MockitoBean
    private lateinit var repo: IApiKeyRepository

    @MockitoBean
    private lateinit var auditRepo: IApiKeyAuditRepository

    @MockitoBean
    private lateinit var updateLastUsed: UpdateApiKeyLastUsedAt

    @MockitoBean
    private lateinit var logAudit: LogApiKeyAudit

    private lateinit var passwordEncoder: BCryptPasswordEncoder


    private val ip = "192.168.0.10"
    private val path = "/api/check"
    private val apiKey = "123456789012abcdef"
    private val entity = ApiKeyEntity(UUID.randomUUID(), "123456789012", "hashed", null, null)

    @BeforeEach
    fun setup() {
        repo = mock()
        auditRepo = mock()
        logAudit = mock()
        updateLastUsed = mock()
        passwordEncoder = mock()
        validateApiKey = ValidateApiKey(repo, auditRepo, logAudit, updateLastUsed, passwordEncoder)
    }

    @Test
    fun `should return null and log when api key too short`() {
        val result = validateApiKey.execute("abc", ip, path)
        assertNull(result)
        verify(logAudit).logFailedAccess(null, ip, path, "Invalid key format")
    }

    @Test
    fun `should return null when key not found`() {
        whenever(repo.findByPublicId(any())).thenReturn(null)
        val key = "123456789012abcd"
        val result = validateApiKey.execute(key, ip, path)
        assertNull(result)
        verify(logAudit).logFailedAccess(null, ip, path, "Key not found")
    }

    @Test
    fun `should return null when bcrypt hash does not match`() {
        val entity = ApiKeyEntity(UUID.randomUUID(), "123456789012", "hashed", null, null)
        whenever(repo.findByPublicId(any())).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(passwordEncoder.matches(any(), any())).thenReturn(false)

        val result = validateApiKey.execute("123456789012abcdef", ip, path)
        assertNull(result)
        verify(logAudit).logFailedAccess(entity.id, ip, path, "Invalid key")
    }

    @Test
    fun `should return ApiKeyEntity on valid key`() {
        val entity = ApiKeyEntity(UUID.randomUUID(), "123456789012", "hashed", null, null)
        whenever(repo.findByPublicId(any())).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(passwordEncoder.matches(any(), any())).thenReturn(true)

        val result = validateApiKey.execute("123456789012abcdef", ip, path)
        assertNotNull(result)
        verify(logAudit).logSuccessfulAccess(entity.id, ip, path)
        verify(updateLastUsed).execute(any())
    }

    @Test
    fun `should throw IllegalArgumentException on blank api key`() {
        assertThrows<IllegalArgumentException> {
            validateApiKey.execute("", ip, path)
        }
    }

    @Test
    fun `should handle master key without querying repository`() {
        // simulate environment master key
        val field = ValidateApiKey::class.java.getDeclaredField("masterKeyFromEnv")
        field.isAccessible = true
        field.set(validateApiKey, "MASTER123")

        val result = validateApiKey.execute("MASTER123", ip, path)
        assertNull(result)
        verify(logAudit).logSuccessfulAccess(null, ip, path)
        verifyNoInteractions(repo)
    }

    @Test
    fun `should propagate exceptions from repository`() {
        whenever(repo.findByPublicId(any())).thenThrow(RuntimeException("DB failure"))
        assertThrows<RuntimeException> {
            validateApiKey.execute("123456789012abcdef", ip, path)
        }
    }

    @Test
    fun `should throw RateLimitException when API key exceeds per-minute limit`() {
        whenever(repo.findByPublicId(any())).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(passwordEncoder.matches(any(), any())).thenReturn(true)
        whenever(
            auditRepo.countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(
                any(), any()
            )
        ).thenReturn(1000000)

        assertThrows<RateLimitException> {
            validateApiKey.execute(apiKey, ip, path)
        }

        verify(auditRepo, times(1)).countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(any(), any())
        verify(logAudit, never()).logSuccessfulAccess(any(), any(), any())
    }

    @Test
    fun `should throw RateLimitException when API key exceeds per-hour limit`() {
        whenever(repo.findByPublicId(any())).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(passwordEncoder.matches(any(), any())).thenReturn(true)
        // first call returns under minute limit, second call returns over hour limit
        whenever(
            auditRepo.countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(
                eq(entity.id), any()
            )
        ).thenReturn(1)
            .thenReturn(100000000)

        assertThrows<RateLimitException> {
            validateApiKey.execute(apiKey, ip, path)
        }

        verify(auditRepo, atLeast(2)).countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(eq(entity.id), any())
        verify(logAudit, never()).logSuccessfulAccess(any(), any(), any())
    }

    @Test
    fun `should throw RateLimitException when IP exceeds global per-minute limit`() {
        whenever(repo.findByPublicId(any())).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(passwordEncoder.matches(any(), any())).thenReturn(true)
        whenever(
            auditRepo.countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(
                eq(entity.id), any()
            )
        ).thenReturn(0)

        whenever(
            auditRepo.findByIpAddressAndTimestampAfter(eq(ip), any())
        ).thenReturn(List(validateApiKey.MAX_REQUESTS_PER_MINUTE + 1) { mock() })

        assertThrows<RateLimitException> {
            validateApiKey.execute(apiKey, ip, path)
        }

        verify(auditRepo).findByIpAddressAndTimestampAfter(eq(ip), any())
        verify(logAudit, never()).logSuccessfulAccess(any(), any(), any())
    }

    @Test
    fun `should pass when rate limits are not exceeded`() {
        whenever(repo.findByPublicId(any())).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(passwordEncoder.matches(any(), any())).thenReturn(true)
        whenever(
            auditRepo.countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(any(), any())
        ).thenReturn(0)
        whenever(
            auditRepo.findByIpAddressAndTimestampAfter(eq(ip), any())
        ).thenReturn(emptyList())

        val result = validateApiKey.execute(apiKey, ip, path)

        assertNotNull(result)
        verify(logAudit).logSuccessfulAccess(entity.id, ip, path)
    }
}

