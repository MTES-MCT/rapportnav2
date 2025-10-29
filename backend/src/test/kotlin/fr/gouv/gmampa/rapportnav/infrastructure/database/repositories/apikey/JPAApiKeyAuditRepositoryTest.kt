package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.apikey.JPAApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey.IDBApiKeyAuditRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.*

class JPAApiKeyAuditRepositoryTest {
    private lateinit var dbRepo: IDBApiKeyAuditRepository
    private lateinit var jpaRepo: JPAApiKeyAuditRepository

    private val sampleEntity = ApiKeyAuditModel(
        id = 1,
        apiKeyId = UUID.randomUUID(),
        ipAddress = "abcd1234",
        requestPath = "..",
        success = false,
    )

    private val apiKeyId = UUID.randomUUID()
    private val now = Instant.now()
    private val audits = listOf(
        ApiKeyAuditModel(apiKeyId = apiKeyId, ipAddress = "1.2.3.4", requestPath = "/test", success = true),
        ApiKeyAuditModel(apiKeyId = apiKeyId, ipAddress = "1.2.3.5", requestPath = "/test2", success = false)
    )

    @BeforeEach
    fun setUp() {
        dbRepo = mock()
        jpaRepo = JPAApiKeyAuditRepository(dbRepo)
    }

    @Test
    fun `save should convert entity to model and delegate`() {
        whenever(dbRepo.save(any())).thenReturn(sampleEntity)

        val result = jpaRepo.save(sampleEntity)
        assertNotNull(result)
        assertEquals(sampleEntity.id, result.id)
        assertEquals(sampleEntity.apiKeyId, result.apiKeyId)
        verify(dbRepo).save(any())
    }

    @Test
    fun `findByApiKeyIdAndTimestampAfter should delegate and return results`() {
        whenever(dbRepo.findByApiKeyIdAndTimestampAfter(apiKeyId, now)).thenReturn(audits)
        val result = jpaRepo.findByApiKeyIdAndTimestampAfter(apiKeyId, now)
        assertEquals(audits, result)
    }

    @Test
    fun `findByApiKeyIdAndTimestampAfter should return empty list when none found`() {
        whenever(dbRepo.findByApiKeyIdAndTimestampAfter(apiKeyId, now)).thenReturn(emptyList())
        val result = jpaRepo.findByApiKeyIdAndTimestampAfter(apiKeyId, now)
        assertEquals(emptyList<ApiKeyAuditModel>(), result)
    }
}
