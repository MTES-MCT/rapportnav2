package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.DisableApiKey
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

@SpringBootTest(classes = [DisableApiKey::class])
@ContextConfiguration(classes = [DisableApiKey::class])
class DisableApiKeyTest {

    @Autowired
    private lateinit var disableApiKey: DisableApiKey

    @MockitoBean
    private lateinit var repo: IApiKeyRepository

    @BeforeEach
    fun setUp() {
        repo = mock()
        disableApiKey = DisableApiKey(repo)
    }

    @Test
    fun `should disable existing enabled api key`() {
        // given
        val publicId = "public123"
        val entity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = publicId,
            hashedKey = "hashed",
            owner = "owner1",
            disabledAt = null
        )

        whenever(repo.findByPublicId(publicId)).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(repo.save(any())).thenReturn(ApiKeyModel.fromApiKeyEntity(entity.copy(disabledAt = Instant.now())))

        // when
        val result = disableApiKey.execute(publicId)

        // then
        assertTrue(result)
        argumentCaptor<ApiKeyModel>().apply {
            verify(repo).save(capture())
            assertNotNull(firstValue.disabledAt)
        }
        verify(repo).findByPublicId(publicId)
    }

    @Test
    fun `should return false if api key not found`() {
        // given
        whenever(repo.findByPublicId(any())).thenReturn(null)

        // when
        val result = disableApiKey.execute("missing-key")

        // then
        assertFalse(result)
        verify(repo).findByPublicId("missing-key")
        verify(repo, never()).save(any())
    }

    @Test
    fun `should return false if api key already disabled`() {
        val publicId = "alreadyDisabled"
        val disabledEntity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = publicId,
            hashedKey = "hashed",
            owner = "owner1",
            disabledAt = Instant.now()
        )

        whenever(repo.findByPublicId(publicId)).thenReturn(ApiKeyModel.fromApiKeyEntity(disabledEntity))

        val result = disableApiKey.execute(publicId)

        assertFalse(result)
        verify(repo, never()).save(any())
    }

    @Test
    fun `should return false if repo save fails`() {
        val publicId = "failingKey"
        val entity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = publicId,
            hashedKey = "hashed",
            owner = "owner"
        )

        whenever(repo.findByPublicId(publicId)).thenReturn(ApiKeyModel.fromApiKeyEntity(entity))
        whenever(repo.save(any())).thenThrow(RuntimeException("DB error"))

        val result = disableApiKey.execute(publicId)

        assertFalse(result)
        verify(repo).findByPublicId(publicId)
        verify(repo).save(any())
    }


}
