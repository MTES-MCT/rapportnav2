package fr.gouv.gmampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.CreateApiKey
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.ValidateApiKey
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [CreateApiKey::class])
@ContextConfiguration(classes = [CreateApiKey::class])
class CreateApiKeyTest {

    @Autowired
    private lateinit var createApiKey: CreateApiKey

    @MockitoBean
    private lateinit var repo: IApiKeyRepository

    @MockitoBean
    private lateinit var validateApiKey: ValidateApiKey

    @MockitoBean
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    private val generatedKey = "abcd1234efgh5678ijkl9012"

    @BeforeEach
    fun setUp() {
        repo = mock()
        validateApiKey = mock()
        passwordEncoder = spy(BCryptPasswordEncoder(12))
        createApiKey = spy(CreateApiKey(repo, validateApiKey, passwordEncoder))
    }

    @Test
    fun `should create api key successfully`() {

        `when`(createApiKey.generateSecureKey()).thenReturn(generatedKey)

        val savedEntity = ApiKeyModel(
            id = UUID.randomUUID(),
            publicId = generatedKey.take(12),
            hashedKey = passwordEncoder.encode(generatedKey) ?: "",
            owner = "owner"
        )

        whenever(repo.save(any<ApiKeyModel>())).thenReturn(savedEntity)
        val (entity, rawKey) = createApiKey.execute(null, "owner")

        assertEquals(savedEntity.publicId, entity?.publicId)
        assertEquals(generatedKey, rawKey)
        assertTrue(passwordEncoder.matches(rawKey, entity?.hashedKey))
        verify(repo, times(1)).save(any())
        verify(createApiKey, times(2)).generateSecureKey()
    }

    @Test
    fun `should use provided valid UUID`() {
        val uuid = UUID.randomUUID()
        whenever(createApiKey.generateSecureKey()).thenReturn(generatedKey)
        whenever(repo.save(any())).thenAnswer { it.arguments[0] as ApiKeyModel }

        val (entity, _) = createApiKey.execute(id = uuid, owner = "bob")

        assertEquals(uuid, entity?.id)
    }

    @Test
    fun `should generate new UUID when null provided`() {
        val invalidUUID = null
        whenever(createApiKey.generateSecureKey()).thenReturn(generatedKey)
        whenever(repo.save(any())).thenAnswer { it.arguments[0] as ApiKeyModel }

        val (entity, _) = createApiKey.execute(id = invalidUUID, owner = "bob")

        assertNotEquals(invalidUUID, entity?.id)
    }

    @Test
    fun `should throw exception when repo save fails`() {
        whenever(createApiKey.generateSecureKey()).thenReturn("abcdefghijklmnopqrstuvwx")
        whenever(repo.save(any())).thenThrow(RuntimeException("DB down"))

        val ex = assertThrows<IllegalStateException> {
            createApiKey.execute(null, "owner")
        }
        assertTrue(ex.message!!.contains("Failed to create API key"))
    }

    @Test
    fun `should throw exception when generated key too short`() {
        whenever(createApiKey.generateSecureKey()).thenReturn("short")

        assertThrows<IllegalStateException> {
            createApiKey.execute(null, "owner")
        }
    }
}
