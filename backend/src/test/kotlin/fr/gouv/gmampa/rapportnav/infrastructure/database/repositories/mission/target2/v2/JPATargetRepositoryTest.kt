package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2.IDBTargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.target2.v2.JPATargetRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPATargetRepository::class])
class JPATargetRepositoryTest {

    @MockitoBean
    private lateinit var dbTargetRepository: IDBTargetRepository

    private lateinit var jpaTargetRepository: JPATargetRepository

    private val targetId = UUID.randomUUID()
    private val actionId = "action-123"
    private val targetModel = TargetModel(
        id = targetId,
        actionId = actionId,
        targetType = TargetType.VEHICLE
    )

    @BeforeEach
    fun setUp() {
        jpaTargetRepository = JPATargetRepository(dbTargetRepository)
    }

    @Test
    fun `findById should return target when found`() {
        `when`(dbTargetRepository.findById(targetId)).thenReturn(Optional.of(targetModel))

        val result = jpaTargetRepository.findById(targetId)

        assertThat(result).isPresent
        assertThat(result.get().id).isEqualTo(targetId)
        verify(dbTargetRepository).findById(targetId)
    }

    @Test
    fun `findById should return empty optional when not found`() {
        val unknownId = UUID.randomUUID()
        `when`(dbTargetRepository.findById(unknownId)).thenReturn(Optional.empty())

        val result = jpaTargetRepository.findById(unknownId)

        assertThat(result).isEmpty
        verify(dbTargetRepository).findById(unknownId)
    }

    @Test
    fun `findByActionId should return list of targets`() {
        `when`(dbTargetRepository.findByActionId(actionId)).thenReturn(listOf(targetModel))

        val result = jpaTargetRepository.findByActionId(actionId)

        assertThat(result).hasSize(1)
        assertThat(result[0].actionId).isEqualTo(actionId)
        verify(dbTargetRepository).findByActionId(actionId)
    }

    @Test
    fun `findByActionId should return empty list when none found`() {
        `when`(dbTargetRepository.findByActionId("unknown")).thenReturn(emptyList())

        val result = jpaTargetRepository.findByActionId("unknown")

        assertThat(result).isEmpty()
    }

    @Test
    fun `findByExternalId should return target when found`() {
        val externalId = "ext-456"
        `when`(dbTargetRepository.findByExternalId(externalId)).thenReturn(targetModel)

        val result = jpaTargetRepository.findByExternalId(externalId)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(targetId)
        verify(dbTargetRepository).findByExternalId(externalId)
    }

    @Test
    fun `findByExternalId should return null when not found`() {
        `when`(dbTargetRepository.findByExternalId("unknown")).thenReturn(null)

        val result = jpaTargetRepository.findByExternalId("unknown")

        assertThat(result).isNull()
    }

    @Test
    fun `save should return saved target`() {
        `when`(dbTargetRepository.save(any<TargetModel>())).thenReturn(targetModel)

        val result = jpaTargetRepository.save(targetModel)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(targetId)
        verify(dbTargetRepository).save(targetModel)
    }

    @Test
    fun `save should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
        `when`(dbTargetRepository.save(any<TargetModel>()))
            .thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaTargetRepository.save(targetModel)
        }
        assertThat(exception.message).contains(targetId.toString())
    }

    @Test
    fun `save should return existing target on duplicate external_id constraint violation`() {
        val externalId = "ext-dup"
        val existingTarget = TargetModel(id = UUID.randomUUID(), actionId = actionId, targetType = TargetType.VEHICLE, externalId = externalId)
        val newTarget = TargetModel(id = UUID.randomUUID(), actionId = actionId, targetType = TargetType.VEHICLE, externalId = externalId)
        `when`(dbTargetRepository.save(any<TargetModel>())).thenThrow(DataIntegrityViolationException("Unique index violation"))
        `when`(dbTargetRepository.findByExternalId(externalId)).thenReturn(existingTarget)

        val result = jpaTargetRepository.save(newTarget)

        assertThat(result.id).isEqualTo(existingTarget.id)
    }

    @Test
    fun `save should throw BackendInternalException on constraint violation without external_id`() {
        `when`(dbTargetRepository.save(any<TargetModel>())).thenThrow(DataIntegrityViolationException("Constraint violation"))

        val exception = assertThrows<BackendInternalException> {
            jpaTargetRepository.save(targetModel)
        }
        assertThat(exception.message).contains(targetId.toString())
    }

    @Test
    fun `save should throw BackendInternalException on unexpected error`() {
        `when`(dbTargetRepository.save(any<TargetModel>()))
            .thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaTargetRepository.save(targetModel)
        }
        assertThat(exception.message).contains("Unable to prepare data before saving")
    }

    @Test
    fun `deleteByActionId should delegate to db repository`() {
        jpaTargetRepository.deleteByActionId(actionId)

        verify(dbTargetRepository).deleteByActionId(actionId)
    }

    @Test
    fun `deleteById should delegate to db repository`() {
        jpaTargetRepository.deleteById(targetId)

        verify(dbTargetRepository).deleteById(targetId)
    }
}