package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.sati.IDBSatiRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.sati.JPASatiRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPASatiRepository::class])
class JPASatiRepositoryTest {

    @MockitoBean
    private lateinit var dbRepo: IDBSatiRepository

    private lateinit var jpaSatiRepository: JPASatiRepository

    private val satiId = UUID.randomUUID()
    private val actionId = "action-sati-1"
    private val now = Instant.now()

    private val satiModel = SatiModel(
        id = satiId,
        module = "T1",
        actionId = actionId
    )

    private val satiEntity = SatiEntity(
        id = satiId,
        module = SatiModuleType.M1,
        actionId = actionId
    )

    @BeforeEach
    fun setUp() {
        jpaSatiRepository = JPASatiRepository(dbRepo)
    }

    // --- findById ---

    @Test
    fun `findById should return entity when found`() {
        `when`(dbRepo.findById(satiId)).thenReturn(Optional.of(satiModel))

        val result = jpaSatiRepository.findById(satiId)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(satiId)
        verify(dbRepo).findById(satiId)
    }

    @Test
    fun `findById should return null when not found`() {
        `when`(dbRepo.findById(satiId)).thenReturn(Optional.empty())

        val result = jpaSatiRepository.findById(satiId)

        assertThat(result).isNull()
    }

    @Test
    fun `findById should throw BackendInternalException on error`() {
        `when`(dbRepo.findById(satiId)).thenThrow(RuntimeException("DB error"))

        val exception = assertThrows<BackendInternalException> {
            jpaSatiRepository.findById(satiId)
        }
        assertThat(exception.message).contains("findById")
    }

    // --- findByActionId ---

    @Test
    fun `findByActionId should return entity when found`() {
        `when`(dbRepo.findByActionId(actionId)).thenReturn(satiModel)

        val result = jpaSatiRepository.findByActionId(actionId)

        assertThat(result).isNotNull
        assertThat(result?.actionId).isEqualTo(actionId)
        verify(dbRepo).findByActionId(actionId)
    }

    @Test
    fun `findByActionId should return null when not found`() {
        `when`(dbRepo.findByActionId(actionId)).thenReturn(null)

        val result = jpaSatiRepository.findByActionId(actionId)

        assertThat(result).isNull()
    }

    @Test
    fun `findByActionId should throw BackendInternalException on error`() {
        `when`(dbRepo.findByActionId(actionId)).thenThrow(RuntimeException("DB error"))

        val exception = assertThrows<BackendInternalException> {
            jpaSatiRepository.findByActionId(actionId)
        }
        assertThat(exception.message).contains("findByOwnerId")
    }

    // --- findAll ---

    @Test
    fun `findAll should return list of entities`() {
        `when`(dbRepo.findAll()).thenReturn(listOf(satiModel))

        val result = jpaSatiRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(satiId)
    }

    @Test
    fun `findAll should return empty list when none found`() {
        `when`(dbRepo.findAll()).thenReturn(emptyList())

        val result = jpaSatiRepository.findAll()

        assertThat(result).isEmpty()
    }

    @Test
    fun `findAll should throw BackendInternalException on error`() {
        `when`(dbRepo.findAll()).thenThrow(RuntimeException("DB error"))

        val exception = assertThrows<BackendInternalException> {
            jpaSatiRepository.findAll()
        }
        assertThat(exception.message).contains("findAll")
    }

    // --- save ---

    @Test
    fun `save should return saved entity`() {
        `when`(dbRepo.save(any<SatiModel>())).thenReturn(satiModel)

        val result = jpaSatiRepository.save(satiEntity)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(satiId)
    }

    @Test
    fun `save should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
        `when`(dbRepo.save(any<SatiModel>()))
            .thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaSatiRepository.save(satiEntity)
        }
        assertThat(exception.message).contains(satiId.toString())
    }

    @Test
    fun `save should throw BackendInternalException on unexpected error`() {
        `when`(dbRepo.save(any<SatiModel>()))
            .thenThrow(RuntimeException("DB error"))

        val exception = assertThrows<BackendInternalException> {
            jpaSatiRepository.save(satiEntity)
        }
        assertThat(exception.message).contains("save failed")
    }

    // --- deleteById ---

    @Test
    fun `deleteById should delegate to db repository`() {
        jpaSatiRepository.deleteById(satiId)

        verify(dbRepo).deleteById(satiId)
    }

    @Test
    fun `deleteById should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
        `when`(dbRepo.deleteById(satiId))
            .thenThrow(InvalidDataAccessApiUsageException("Invalid id"))

        val exception = assertThrows<BackendUsageException> {
            jpaSatiRepository.deleteById(satiId)
        }
        assertThat(exception.message).contains("deleteById")
    }

    @Test
    fun `deleteById should throw BackendInternalException on unexpected error`() {
        `when`(dbRepo.deleteById(satiId))
            .thenThrow(RuntimeException("DB error"))

        val exception = assertThrows<BackendInternalException> {
            jpaSatiRepository.deleteById(satiId)
        }
        assertThat(exception.message).contains("deleteById")
    }

    // --- existsById ---

    @Test
    fun `existsById should return true when exists`() {
        `when`(dbRepo.existsById(satiId)).thenReturn(true)

        val result = jpaSatiRepository.existsById(satiId)

        assertThat(result).isTrue()
        verify(dbRepo).existsById(satiId)
    }

    @Test
    fun `existsById should return false when not exists`() {
        `when`(dbRepo.existsById(satiId)).thenReturn(false)

        val result = jpaSatiRepository.existsById(satiId)

        assertThat(result).isFalse()
    }

    @Test
    fun `existsById should throw BackendInternalException on error`() {
        `when`(dbRepo.existsById(satiId)).thenThrow(RuntimeException("DB error"))

        val exception = assertThrows<BackendInternalException> {
            jpaSatiRepository.existsById(satiId)
        }
        assertThat(exception.message).contains("existsById")
    }
}
