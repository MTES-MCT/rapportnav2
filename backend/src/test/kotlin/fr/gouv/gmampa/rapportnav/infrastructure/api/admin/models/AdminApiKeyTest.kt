package fr.gouv.gmampa.rapportnav.infrastructure.api.admin.models

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.models.AdminApiKey
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class AdminApiKeyTest {

    @Test
    fun `fromApiKeyEntity should map all fields`() {
        val id = UUID.randomUUID()
        val now = Instant.now()
        val entity = ApiKeyEntity(
            id = id,
            publicId = "abc123",
            hashedKey = "hashed",
            owner = "test-service",
            createdAt = now,
            updatedAt = now,
            lastUsedAt = now,
            disabledAt = now
        )

        val result = AdminApiKey.fromApiKeyEntity(entity)

        assertThat(result.id).isEqualTo(id)
        assertThat(result.publicId).isEqualTo("abc123")
        assertThat(result.owner).isEqualTo("test-service")
        assertThat(result.createdAt).isEqualTo(now)
        assertThat(result.updatedAt).isEqualTo(now)
        assertThat(result.lastUsedAt).isEqualTo(now)
        assertThat(result.disabledAt).isEqualTo(now)
    }

    @Test
    fun `fromApiKeyEntity should handle null optional fields`() {
        val id = UUID.randomUUID()
        val entity = ApiKeyEntity(
            id = id,
            publicId = "xyz789",
            hashedKey = "hashed"
        )

        val result = AdminApiKey.fromApiKeyEntity(entity)

        assertThat(result.id).isEqualTo(id)
        assertThat(result.publicId).isEqualTo("xyz789")
        assertThat(result.owner).isNull()
        assertThat(result.createdAt).isNull()
        assertThat(result.updatedAt).isNull()
        assertThat(result.lastUsedAt).isNull()
        assertThat(result.disabledAt).isNull()
    }
}