package fr.gouv.gmampa.rapportnav.infrastructure.database.model.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class ApiKeyModelTest {

    private val now = Instant.now()

    private val entity = ApiKeyEntity(
        id = UUID.randomUUID(),
        publicId = "abcd1234",
        hashedKey = "hashed",
        owner = "user1",
        createdAt = now,
        updatedAt = now,
        lastUsedAt = now,
        disabledAt = null
    )

    @Test
    fun `toApiKeyEntity should map all fields correctly`() {
        val model = ApiKeyModel.fromApiKeyEntity(entity)
        val result = model.toApiKeyEntity()

        assertEquals(entity.id, result.id)
        assertEquals(entity.publicId, result.publicId)
        assertEquals(entity.hashedKey, result.hashedKey)
        assertEquals(entity.owner, result.owner)
        assertEquals(entity.createdAt, result.createdAt)
        assertEquals(entity.updatedAt, result.updatedAt)
        assertEquals(entity.lastUsedAt, result.lastUsedAt)
        assertEquals(entity.disabledAt, result.disabledAt)
    }

    @Test
    fun `fromApiKeyEntity should map all fields correctly`() {
        val model = ApiKeyModel.fromApiKeyEntity(entity)

        assertEquals(entity.id, model.id)
        assertEquals(entity.publicId, model.publicId)
        assertEquals(entity.hashedKey, model.hashedKey)
        assertEquals(entity.owner, model.owner)
        assertEquals(entity.createdAt, model.createdAt)
        assertEquals(entity.updatedAt, model.updatedAt)
        assertEquals(entity.lastUsedAt, model.lastUsedAt)
        assertEquals(entity.disabledAt, model.disabledAt)
    }
}
