package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllMissionActions
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [GetAllMissionActions::class])
class GetAllMissionActionsTest {

    @Autowired
    private lateinit var getAllMissionActions: GetAllMissionActions

    @MockitoBean
    private lateinit var repository: INavMissionActionRepository

    private fun action(id: UUID = UUID.randomUUID(), ownerId: UUID? = null, start: Instant = Instant.parse("2025-01-01T00:00:00Z")): MissionActionModel =
        MissionActionModel(id = id, ownerId = ownerId, actionType = ActionType.CONTROL, startDateTimeUtc = start)

    @Test
    fun `should return paginated actions`() {
        val newer = action(start = Instant.parse("2025-01-02T00:00:00Z"))
        val older = action(start = Instant.parse("2025-01-01T00:00:00Z"))
        val page = PageImpl(listOf(newer, older), PageRequest.of(0, 10), 2)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllMissionActions.execute(0, 10, null, null)

        assertThat(result.content).hasSize(2)
        assertThat(result.content[0].id).isEqualTo(newer.id)
        assertThat(result.totalElements).isEqualTo(2)
        verify(repository).findAllPaginated(0, 10)
    }

    @Test
    fun `should return all when both searches are null`() {
        val page = PageImpl(listOf(action()), PageRequest.of(0, 10), 1)
        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllMissionActions.execute(0, 10, null, null)

        assertThat(result.content).hasSize(1)
        verify(repository).findAllPaginated(0, 10)
    }

    @Test
    fun `should return all when both searches are blank`() {
        val page = PageImpl(listOf(action()), PageRequest.of(0, 10), 1)
        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllMissionActions.execute(0, 10, "   ", "  ")

        assertThat(result.content).hasSize(1)
        verify(repository).findAllPaginated(0, 10)
    }

    @Test
    fun `should search by id when searchId is a UUID`() {
        val id = UUID.randomUUID()
        val page = PageImpl(listOf(action(id = id)), PageRequest.of(0, 10), 1)
        whenever(repository.findByIdPaginated(id, 0, 10)).thenReturn(page)

        val result = getAllMissionActions.execute(0, 10, id.toString(), null)

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].id).isEqualTo(id)
        verify(repository).findByIdPaginated(id, 0, 10)
    }

    @Test
    fun `should trim searchId before matching`() {
        val id = UUID.randomUUID()
        val page = PageImpl(listOf(action(id = id)), PageRequest.of(0, 10), 1)
        whenever(repository.findByIdPaginated(id, 0, 10)).thenReturn(page)

        val result = getAllMissionActions.execute(0, 10, "  $id  ", null)

        assertThat(result.content).hasSize(1)
        verify(repository).findByIdPaginated(id, 0, 10)
    }

    @Test
    fun `should search by ownerId when only searchOwnerId is a UUID`() {
        val ownerId = UUID.randomUUID()
        val page = PageImpl(listOf(action(ownerId = ownerId)), PageRequest.of(0, 10), 1)
        whenever(repository.findByOwnerIdPaginated(ownerId, 0, 10)).thenReturn(page)

        val result = getAllMissionActions.execute(0, 10, null, ownerId.toString())

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].ownerId).isEqualTo(ownerId)
        verify(repository).findByOwnerIdPaginated(ownerId, 0, 10)
    }

    @Test
    fun `searchId should take precedence over searchOwnerId when both are set`() {
        val id = UUID.randomUUID()
        val ownerId = UUID.randomUUID()
        val page = PageImpl(listOf(action(id = id)), PageRequest.of(0, 10), 1)
        whenever(repository.findByIdPaginated(id, 0, 10)).thenReturn(page)

        val result = getAllMissionActions.execute(0, 10, id.toString(), ownerId.toString())

        assertThat(result.content).hasSize(1)
        verify(repository).findByIdPaginated(id, 0, 10)
        verify(repository, never()).findByOwnerIdPaginated(ownerId, 0, 10)
    }

    @Test
    fun `should return empty page when searchId is not a UUID`() {
        val result = getAllMissionActions.execute(0, 10, "not-a-uuid", null)

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
        verify(repository, never()).findAllPaginated(0, 10)
    }

    @Test
    fun `should return empty page when searchOwnerId is not a UUID`() {
        val result = getAllMissionActions.execute(0, 10, null, "not-a-uuid")

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
        verify(repository, never()).findAllPaginated(0, 10)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )
        whenever(repository.findAllPaginated(0, 10)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            getAllMissionActions.execute(0, 10, null, null)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
