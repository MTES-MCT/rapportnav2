package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.CreateApiKey
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RotateApiKey
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [RotateApiKey::class])
@ContextConfiguration(classes = [RotateApiKey::class])
class RotateApiKeyTest {

    @Autowired
    private lateinit var rotateApiKey: RotateApiKey

    @MockitoBean
    private lateinit var createApiKey: CreateApiKey

    @MockitoBean
    private lateinit var repo: IApiKeyRepository

    @BeforeEach
    fun setUp() {
        repo = mock()
        rotateApiKey = RotateApiKey(repo, createApiKey)
    }

    @Test
    fun `should rotate api key successfully`() {
        // given
        val oldPublicId = "oldPublic1234"
        val owner = "owner1"
        val existingEntity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = oldPublicId,
            hashedKey = "oldHashed",
            owner = owner
        )

        val newEntity = ApiKeyEntity(
            id = existingEntity.id,
            publicId = "newPublic5678",
            hashedKey = "newHashed",
            owner = owner
        )
        val newRawKey = "rawKeyExample"

        whenever(repo.findByPublicId(oldPublicId)).thenReturn(ApiKeyModel.fromApiKeyEntity(existingEntity))
        whenever(createApiKey.execute(existingEntity.id, owner)).thenReturn(newEntity to newRawKey)

        // when
        val (resultEntity, resultRaw) = rotateApiKey.execute(oldPublicId)

        // then
        assertEquals(newEntity.publicId, resultEntity?.publicId)
        assertEquals(newRawKey, resultRaw)
        verify(repo).findByPublicId(oldPublicId)
        verify(createApiKey).execute(existingEntity.id, owner)
    }

    @Test
    fun `should throw when publicId not found`() {
        whenever(repo.findByPublicId(any())).thenReturn(null)

        val ex = assertThrows<NoSuchElementException> {
            rotateApiKey.execute(publicId = "missingId")
        }

        assertTrue(ex.message!!.contains("No API key found"))
        verify(repo).findByPublicId("missingId")
        verifyNoInteractions(createApiKey)
    }

    @Test
    fun `should propagate exception from CreateApiKey`() {
        val oldPublicId = "oldPublic"
        val owner = "bob"
        val existingEntity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = oldPublicId,
            hashedKey = "old",
            owner = owner
        )
        whenever(repo.findByPublicId(oldPublicId)).thenReturn(ApiKeyModel.fromApiKeyEntity(existingEntity))
        whenever(createApiKey.execute(any(), any())).thenThrow(IllegalStateException("creation failed"))

        val ex = assertThrows<IllegalStateException> {
            rotateApiKey.execute(oldPublicId)
        }
        assertEquals("creation failed", ex.cause?.message ?: ex.message)
    }


}
