package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.GetAllApiKeys
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetAllApiKeys::class])
@ContextConfiguration(classes = [GetAllApiKeys::class])
class GetAllApiKeysTest {

    @Autowired
    private lateinit var getAllApiKeys: GetAllApiKeys

    @MockitoBean
    private lateinit var repo: IApiKeyRepository

    @BeforeEach
    fun setUp() {
        repo = mock()
        getAllApiKeys = GetAllApiKeys(repo)
    }

    @Test
    fun `should return all api keys`() {
        // given
        val key1 = ApiKeyEntity(publicId = "pub1", hashedKey = "hash1", owner = "user1")
        val key2 = ApiKeyEntity(publicId = "pub2", hashedKey = "hash2", owner = "user2")
        val models = listOf(
            ApiKeyModel.fromApiKeyEntity(key1),
            ApiKeyModel.fromApiKeyEntity(key2)
        )

        whenever(repo.findAll()).thenReturn(models)

        // when
        val result = getAllApiKeys.execute()

        // then
        assertEquals(2, result.size)
        assertEquals("pub1", result[0].publicId)
        assertEquals("pub2", result[1].publicId)
        verify(repo).findAll()
    }

    @Test
    fun `should return empty list when no api keys exist`() {
        // given
        whenever(repo.findAll()).thenReturn(emptyList())

        // when
        val result = getAllApiKeys.execute()

        // then
        assertTrue(result.isEmpty())
        verify(repo).findAll()
    }

    @Test
    fun `should handle null returned by repo safely`() {
        // given
        whenever(repo.findAll()).thenReturn(null)

        // when
        val result = getAllApiKeys.execute()

        // then
        assertTrue(result.isEmpty())
        verify(repo).findAll()
    }

}
