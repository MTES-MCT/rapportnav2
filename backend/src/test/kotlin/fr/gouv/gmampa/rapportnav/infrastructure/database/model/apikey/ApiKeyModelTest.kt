package fr.gouv.gmampa.rapportnav.infrastructure.database.model.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class ApiKeyAuditModelTest {

    private val apiKeyId = UUID.randomUUID()
    private val now = Instant.now()

    @Test
    fun `should create audit model with success true`() {
        val audit = ApiKeyAuditModel(
            apiKeyId = apiKeyId,
            ipAddress = "1.2.3.4",
            requestPath = "/test",
            success = true
        )

        assertNotNull(audit.timestamp)
        assertEquals(apiKeyId, audit.apiKeyId)
        assertEquals("1.2.3.4", audit.ipAddress)
        assertEquals("/test", audit.requestPath)
        assertEquals(true, audit.success)
        assertNull(audit.failureReason)
    }

    @Test
    fun `should create audit model with failure reason`() {
        val audit = ApiKeyAuditModel(
            apiKeyId = apiKeyId,
            ipAddress = "1.2.3.4",
            requestPath = "/test",
            success = false,
            failureReason = "Invalid key",
            timestamp = now
        )

        assertEquals(now, audit.timestamp)
        assertEquals("Invalid key", audit.failureReason)
        assertEquals(false, audit.success)
    }
}
