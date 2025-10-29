package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.apikey.JPAApiKeyRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey.IDBApiKeyRepository
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

class JPAApiKeyRepositoryTest {
    private lateinit var dbRepo: IDBApiKeyRepository
    private lateinit var jpaRepo: JPAApiKeyRepository

    private val sampleEntity = ApiKeyModel(
        id = UUID.randomUUID(),
        publicId = "abcd1234",
        hashedKey = "hashed",
        owner = "user1",
        createdAt = Instant.now()
    )

    @BeforeEach
    fun setUp() {
        dbRepo = mock()
        jpaRepo = JPAApiKeyRepository(dbRepo)
    }

    @Test
    fun `findById should delegate to repository`() {
        whenever(dbRepo.findById(sampleEntity.id)).thenReturn(Optional.of(sampleEntity))

        val result = jpaRepo.findById(sampleEntity.id)
        assertEquals(sampleEntity, result.get())
    }

    @Test
    fun `findById should return empty when not found`() {
        whenever(dbRepo.findById(sampleEntity.id)).thenReturn(Optional.empty())

        val result = jpaRepo.findById(sampleEntity.id)
        assertEquals(Optional.empty<ApiKeyModel>(), result)
    }

    @Test
    fun `findAll should delegate to repository`() {
        val models = listOf(sampleEntity)
        whenever(dbRepo.findAll()).thenReturn(models)

        val result = jpaRepo.findAll()
        assertEquals(models, result)
    }

    @Test
    fun `findByPublicId should delegate to repository`() {
        whenever(dbRepo.findByPublicId(sampleEntity.publicId)).thenReturn(sampleEntity)

        val result = jpaRepo.findByPublicId(sampleEntity.publicId)
        assertEquals(sampleEntity, result)
    }

    @Test
    fun `save should convert entity to model and delegate`() {
        whenever(dbRepo.save(any())).thenReturn(sampleEntity)

        val result = jpaRepo.save(sampleEntity)
        assertNotNull(result)
        assertEquals(sampleEntity.id, result.id)
        assertEquals(sampleEntity.publicId, result.publicId)
        verify(dbRepo).save(any())
    }
}
