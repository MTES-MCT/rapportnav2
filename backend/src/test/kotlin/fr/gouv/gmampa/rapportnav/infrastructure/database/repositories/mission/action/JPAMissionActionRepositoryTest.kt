package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action.JPAMissionActionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAMissionActionRepository::class])
class JPAMissionActionRepositoryTest {

    @MockitoBean
    private lateinit var dbServiceRepository: IDBMissionActionRepository

    final val id1 = UUID.randomUUID()
    final val id2 = UUID.randomUUID()
    private val missionActions: List<MissionActionModel> = listOf(
        MissionActionModel(
            id = id1,
            ownerId = UUID.randomUUID(),
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        ),
        MissionActionModel(
            id = UUID.randomUUID(),
            ownerId = UUID.randomUUID(),
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        ),
        MissionActionModel(
            id = id2,
            ownerId = UUID.randomUUID(),
            actionType = ActionType.SURVEILLANCE,
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        )
    )

    @Test
    fun `execute should retrieve action by mission id`() {
        val missionId = UUID.randomUUID()
        Mockito.`when`(dbServiceRepository.findAllByOwnerId(missionId)).thenReturn(missionActions)
        val jPAMissionActionRepository = JPAMissionActionRepository(dbServiceRepository)
        val responses = jPAMissionActionRepository.findByMissionId(missionId = missionId)
        assertThat(responses).isNotNull()
        assertThat(responses.size).isEqualTo(3)
        assertThat(responses.map { action -> action.id }).containsAll(listOf(id1, id2))
    }

    @Test
    fun `execute should retrieve action by id`() {
        Mockito.`when`(dbServiceRepository.findById(id1)).thenReturn(Optional.of(missionActions.get(0)))
        val jPAMissionActionRepository = JPAMissionActionRepository(dbServiceRepository)
        val response = jPAMissionActionRepository.findById(id = id1).orElse(null)
        assertThat(response).isNotNull()
        assertThat(response?.id).isEqualTo(id1)
    }

    @Test
    fun `findById should return empty optional when not found`() {
        Mockito.`when`(dbServiceRepository.findById(id1)).thenReturn(Optional.empty())
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val result = repo.findById(id1)
        assertThat(result).isEmpty
    }

    @Test
    fun `findById should throw BackendInternalException on error`() {
        Mockito.`when`(dbServiceRepository.findById(id1)).thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendInternalException> { repo.findById(id1) }
        assertThat(ex.message).contains("Failed to find MissionAction")
    }

    @Test
    fun `findByMissionId should throw BackendInternalException on error`() {
        val missionId = UUID.randomUUID()
        Mockito.`when`(dbServiceRepository.findAllByOwnerId(missionId)).thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendInternalException> { repo.findByMissionId(missionId) }
        assertThat(ex.message).contains("Failed to find MissionActions")
    }

    @Test
    fun `findByOwnerId should return actions`() {
        val ownerId = UUID.randomUUID()
        Mockito.`when`(dbServiceRepository.findAllByOwnerId(ownerId)).thenReturn(missionActions)
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val result = repo.findByOwnerId(ownerId)
        assertThat(result).hasSize(3)
    }

    @Test
    fun `findByOwnerId should throw BackendInternalException on error`() {
        val ownerId = UUID.randomUUID()
        Mockito.`when`(dbServiceRepository.findAllByOwnerId(ownerId)).thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendInternalException> { repo.findByOwnerId(ownerId) }
        assertThat(ex.message).contains("Failed to find MissionActions for ownerId")
    }

    @Test
    fun `save should return saved action`() {
        val action = missionActions[0]
        Mockito.`when`(dbServiceRepository.save(any<MissionActionModel>())).thenReturn(action)
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val result = repo.save(action)
        assertThat(result.id).isEqualTo(id1)
        verify(dbServiceRepository).save(action)
    }

    @Test
    fun `save should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
        val action = missionActions[0]
        Mockito.`when`(dbServiceRepository.save(any<MissionActionModel>()))
            .thenThrow(InvalidDataAccessApiUsageException("Invalid"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendUsageException> { repo.save(action) }
        assertThat(ex.message).contains(id1.toString())
    }

    @Test
    fun `save should throw BackendInternalException on unexpected error`() {
        val action = missionActions[0]
        Mockito.`when`(dbServiceRepository.save(any<MissionActionModel>()))
            .thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendInternalException> { repo.save(action) }
        assertThat(ex.message).contains("Unable to prepare data before saving")
    }

    @Test
    fun `deleteById should delegate to db repository`() {
        val repo = JPAMissionActionRepository(dbServiceRepository)
        repo.deleteById(id1)
        verify(dbServiceRepository).deleteById(id1)
    }

    @Test
    fun `deleteById should throw BackendUsageException on InvalidDataAccessApiUsageException`() {
        Mockito.`when`(dbServiceRepository.deleteById(id1))
            .thenThrow(InvalidDataAccessApiUsageException("Invalid"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendUsageException> { repo.deleteById(id1) }
        assertThat(ex.message).contains("Unable to delete MissionAction")
    }

    @Test
    fun `deleteById should throw BackendInternalException on unexpected error`() {
        Mockito.`when`(dbServiceRepository.deleteById(id1))
            .thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendInternalException> { repo.deleteById(id1) }
        assertThat(ex.message).contains("Failed to delete MissionAction")
    }

    @Test
    fun `existsById should return true when exists`() {
        Mockito.`when`(dbServiceRepository.existsById(id1)).thenReturn(true)
        val repo = JPAMissionActionRepository(dbServiceRepository)
        assertThat(repo.existsById(id1)).isTrue()
    }

    @Test
    fun `existsById should return false when not exists`() {
        Mockito.`when`(dbServiceRepository.existsById(id1)).thenReturn(false)
        val repo = JPAMissionActionRepository(dbServiceRepository)
        assertThat(repo.existsById(id1)).isFalse()
    }

    @Test
    fun `existsById should throw BackendInternalException on error`() {
        Mockito.`when`(dbServiceRepository.existsById(id1)).thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendInternalException> { repo.existsById(id1) }
        assertThat(ex.message).contains("Failed to check existence")
    }
}
