package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.UpdateApiKeyLastUsedAt
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [UpdateApiKeyLastUsedAt::class])
@ContextConfiguration(classes = [UpdateApiKeyLastUsedAt::class])
class UpdateApiKeyLastUsedAtTest {

    @Autowired
    private lateinit var updateApiKeyLastUsedAt: UpdateApiKeyLastUsedAt

    @MockitoBean
    private lateinit var repo: IApiKeyRepository

    @BeforeEach
    fun setUp() {
        repo = mock()
        updateApiKeyLastUsedAt = UpdateApiKeyLastUsedAt(repo)
    }

    @Test
    fun `should update lastUsedAt when api key exists`() {
        // given
        val model = ApiKeyModel(
            id = UUID.randomUUID(),
            publicId = "pub123",
            hashedKey = "hashed",
            owner = "owner"
        )
        whenever(repo.findById(model.id)).thenReturn(Optional.of(model))
        whenever(repo.save(any())).thenAnswer {
            it.arguments[0] as ApiKeyModel
        }

        // when
        val result = updateApiKeyLastUsedAt.execute(model.toApiKeyEntity())

        // then
        assertTrue(result.isPresent)
        val updatedModel = result.get()
        assertNotNull(updatedModel.lastUsedAt)

        argumentCaptor<ApiKeyModel>().apply {
            verify(repo).save(capture())
            assertNotNull(firstValue.lastUsedAt)
            assertTrue(firstValue.lastUsedAt!!.isBefore(Instant.now().plusSeconds(5)))
        }

        verify(repo).findById(model.id)
    }

    @Test
    fun `should return empty optional when api key not found`() {
        // given
        val entity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = "missing",
            hashedKey = "hashed",
            owner = "owner"
        )

        whenever(repo.findById(entity.id)).thenReturn(Optional.empty())

        // when
        val result = updateApiKeyLastUsedAt.execute(entity)

        // then
        assertTrue(result.isEmpty)
        verify(repo).findById(entity.id)
        verify(repo, never()).save(any())
    }

    @Test
    fun `should propagate exception if save fails`() {
        // given
        val entity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = "pubError",
            hashedKey = "hashed",
            owner = "owner"
        )
        val model = ApiKeyModel.fromApiKeyEntity(entity)

        whenever(repo.findById(entity.id)).thenReturn(Optional.of(model))
        whenever(repo.save(any())).thenThrow(RuntimeException("DB error"))

        // when + then
        val ex = assertThrows<RuntimeException> {
            updateApiKeyLastUsedAt.execute(entity)
        }

        assertEquals("DB error", ex.message)
        verify(repo).findById(entity.id)
        verify(repo).save(any())
    }

}
