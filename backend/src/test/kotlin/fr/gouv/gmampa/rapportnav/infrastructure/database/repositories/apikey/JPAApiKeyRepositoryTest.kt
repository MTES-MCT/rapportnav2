package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.apikey.JPAApiKeyRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey.IDBApiKeyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAApiKeyRepository::class])
class JPAApiKeyRepositoryTest {

    @MockitoBean
    private lateinit var dbRepository: IDBApiKeyRepository

    private lateinit var jpaRepository: JPAApiKeyRepository

    private val keyId = UUID.randomUUID()
    private val publicId = "abc123def456"
    private val apiKeyModel = ApiKeyModel(
        id = keyId,
        publicId = publicId,
        hashedKey = "hashed-secret-key",
        owner = "test-service"
    )

    @BeforeEach
    fun setUp() {
        jpaRepository = JPAApiKeyRepository(dbRepository)
    }

    // --- findById ---

    @Test
    fun `findById should return api key when found`() {
        `when`(dbRepository.findById(keyId)).thenReturn(Optional.of(apiKeyModel))

        val result = jpaRepository.findById(keyId)

        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(keyId)
        assertThat(result.get().publicId).isEqualTo(publicId)
        verify(dbRepository).findById(keyId)
    }

    @Test
    fun `findById should return empty optional when not found`() {
        val unknownId = UUID.randomUUID()
        `when`(dbRepository.findById(unknownId)).thenReturn(Optional.empty())

        val result = jpaRepository.findById(unknownId)

        assertThat(result).isEmpty
    }

    // --- findAll ---

    @Test
    fun `findAll should return list of api keys`() {
        `when`(dbRepository.findAll()).thenReturn(listOf(apiKeyModel))

        val result = jpaRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].publicId).isEqualTo(publicId)
        verify(dbRepository).findAll()
    }

    @Test
    fun `findAll should return empty list when no keys`() {
        `when`(dbRepository.findAll()).thenReturn(emptyList())

        val result = jpaRepository.findAll()

        assertThat(result).isEmpty()
    }

    // --- findByPublicId ---

    @Test
    fun `findByPublicId should return api key when found`() {
        `when`(dbRepository.findByPublicId(publicId)).thenReturn(apiKeyModel)

        val result = jpaRepository.findByPublicId(publicId)

        assertThat(result).isNotNull
        assertThat(result?.publicId).isEqualTo(publicId)
        verify(dbRepository).findByPublicId(publicId)
    }

    @Test
    fun `findByPublicId should return null when not found`() {
        `when`(dbRepository.findByPublicId("unknown")).thenReturn(null)

        val result = jpaRepository.findByPublicId("unknown")

        assertThat(result).isNull()
    }

    // --- save ---

    @Test
    fun `save should return saved api key`() {
        `when`(dbRepository.save(any<ApiKeyModel>())).thenReturn(apiKeyModel)

        val result = jpaRepository.save(apiKeyModel)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(keyId)
        verify(dbRepository).save(apiKeyModel)
    }
}