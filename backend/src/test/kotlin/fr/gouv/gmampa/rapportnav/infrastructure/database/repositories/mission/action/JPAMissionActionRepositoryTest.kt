package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action.JPAMissionActionRepository
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
        MissionActionModelMock.create(
            id = id1,
            missionId = 761,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        ),
        MissionActionModelMock.create(
            id = UUID.randomUUID(),
            missionId = 761,
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        ),
        MissionActionModelMock.create(
            id = id2,
            missionId = 761,
            actionType = ActionType.SURVEILLANCE,
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        )
    )

    @Test
    fun `execute should retrieve action by mission id`() {
        Mockito.`when`(dbServiceRepository.findAllByMissionId(761)).thenReturn(missionActions)
        val jPAMissionActionRepository = JPAMissionActionRepository(dbServiceRepository)
        val responses = jPAMissionActionRepository.findByMissionId(missionId = 761)
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
        Mockito.`when`(dbServiceRepository.findAllByMissionId(761)).thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)
        val ex = assertThrows<BackendInternalException> { repo.findByMissionId(761) }
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

    @Test
    fun `findAllPaginated should return paginated actions ordered by start date desc`() {
        val page = PageImpl(missionActions, PageRequest.of(0, 10), missionActions.size.toLong())
        Mockito.`when`(dbServiceRepository.findAllByOrderByStartDateTimeUtcDesc(any<Pageable>())).thenReturn(page)
        val repo = JPAMissionActionRepository(dbServiceRepository)

        val result = repo.findAllPaginated(0, 10)

        assertThat(result.content).hasSize(3)
        assertThat(result.totalElements).isEqualTo(3)
        verify(dbServiceRepository).findAllByOrderByStartDateTimeUtcDesc(PageRequest.of(0, 10))
    }

    @Test
    fun `findAllPaginated should throw BackendInternalException on error`() {
        Mockito.`when`(dbServiceRepository.findAllByOrderByStartDateTimeUtcDesc(any<Pageable>()))
            .thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)

        val ex = assertThrows<BackendInternalException> { repo.findAllPaginated(0, 10) }
        assertThat(ex.message).contains("Failed to find paginated MissionAction")
    }

    @Test
    fun `findByIdPaginated should return paginated actions matching the id`() {
        val page = PageImpl(listOf(missionActions[0]), PageRequest.of(0, 10), 1)
        Mockito.`when`(dbServiceRepository.findByIdOrderByStartDateTimeUtcDesc(any<UUID>(), any<Pageable>()))
            .thenReturn(page)
        val repo = JPAMissionActionRepository(dbServiceRepository)

        val result = repo.findByIdPaginated(id1, 0, 10)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].id).isEqualTo(id1)
        verify(dbServiceRepository).findByIdOrderByStartDateTimeUtcDesc(id1, PageRequest.of(0, 10))
    }

    @Test
    fun `findByIdPaginated should throw BackendInternalException on error`() {
        Mockito.`when`(dbServiceRepository.findByIdOrderByStartDateTimeUtcDesc(any<UUID>(), any<Pageable>()))
            .thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)

        val ex = assertThrows<BackendInternalException> { repo.findByIdPaginated(id1, 0, 10) }
        assertThat(ex.message).contains("Failed to find paginated MissionAction by id")
    }

    @Test
    fun `findByOwnerIdPaginated should return paginated actions matching the owner id`() {
        val ownerId = UUID.randomUUID()
        val page = PageImpl(missionActions, PageRequest.of(0, 10), missionActions.size.toLong())
        Mockito.`when`(dbServiceRepository.findByOwnerIdOrderByStartDateTimeUtcDesc(any<UUID>(), any<Pageable>()))
            .thenReturn(page)
        val repo = JPAMissionActionRepository(dbServiceRepository)

        val result = repo.findByOwnerIdPaginated(ownerId, 0, 10)

        assertThat(result.content).hasSize(3)
        verify(dbServiceRepository).findByOwnerIdOrderByStartDateTimeUtcDesc(ownerId, PageRequest.of(0, 10))
    }

    @Test
    fun `findByOwnerIdPaginated should throw BackendInternalException on error`() {
        val ownerId = UUID.randomUUID()
        Mockito.`when`(dbServiceRepository.findByOwnerIdOrderByStartDateTimeUtcDesc(any<UUID>(), any<Pageable>()))
            .thenThrow(RuntimeException("DB error"))
        val repo = JPAMissionActionRepository(dbServiceRepository)

        val ex = assertThrows<BackendInternalException> { repo.findByOwnerIdPaginated(ownerId, 0, 10) }
        assertThat(ex.message).contains("Failed to find paginated MissionAction by ownerId")
    }
}
