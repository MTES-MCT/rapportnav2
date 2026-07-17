package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllMissions
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [GetAllMissions::class])
class GetAllMissionsTest {

    @Autowired
    private lateinit var getAllMissions: GetAllMissions

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    private fun mission(start: Instant): MissionModel =
        MissionModel(id = UUID.randomUUID(), serviceId = 1, startDateTimeUtc = start)

    @Test
    fun `should return paginated missions`() {
        val newer = mission(Instant.parse("2025-01-02T00:00:00Z"))
        val older = mission(Instant.parse("2025-01-01T00:00:00Z"))
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(newer, older), pageable, 2)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllMissions.execute(0, 10)

        assertThat(result.content).hasSize(2)
        assertThat(result.content[0].id).isEqualTo(newer.id)
        assertThat(result.totalElements).isEqualTo(2)
    }

    @Test
    fun `should return empty page when no missions`() {
        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(emptyList<MissionModel>(), pageable, 0)

        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllMissions.execute(0, 10)

        assertThat(result.content).isEmpty()
        assertThat(result.totalElements).isEqualTo(0)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        whenever(repository.findAllPaginated(0, 10)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            getAllMissions.execute(0, 10)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }

    @Test
    fun `should return all when search is null`() {
        val page = PageImpl(listOf(mission(Instant.parse("2025-01-01T00:00:00Z"))), PageRequest.of(0, 10), 1)
        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllMissions.execute(0, 10, null)

        assertThat(result.content).hasSize(1)
        verify(repository).findAllPaginated(0, 10)
    }

    @Test
    fun `should return all when search is blank`() {
        val page = PageImpl(listOf(mission(Instant.parse("2025-01-01T00:00:00Z"))), PageRequest.of(0, 10), 1)
        whenever(repository.findAllPaginated(0, 10)).thenReturn(page)

        val result = getAllMissions.execute(0, 10, "   ")

        assertThat(result.content).hasSize(1)
        verify(repository).findAllPaginated(0, 10)
    }

    @Test
    fun `should search by id when search is a UUID`() {
        val id = UUID.randomUUID()
        val model = MissionModel(id = id, serviceId = 1, startDateTimeUtc = Instant.parse("2025-01-01T00:00:00Z"))
        val page = PageImpl(listOf(model), PageRequest.of(0, 10), 1)
        whenever(repository.findByIdPaginated(id, 0, 10)).thenReturn(page)

        val result = getAllMissions.execute(0, 10, id.toString())

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].id).isEqualTo(id)
        verify(repository).findByIdPaginated(id, 0, 10)
    }

    @Test
    fun `should search by externalId when search is not a UUID`() {
        val model = MissionModel(
            id = UUID.randomUUID(),
            serviceId = 1,
            externalId = "12345",
            startDateTimeUtc = Instant.parse("2025-01-01T00:00:00Z")
        )
        val page = PageImpl(listOf(model), PageRequest.of(0, 10), 1)
        whenever(repository.findByExternalIdPaginated("12345", 0, 10)).thenReturn(page)

        val result = getAllMissions.execute(0, 10, "12345")

        assertThat(result.content).hasSize(1)
        assertThat(result.content[0].externalId).isEqualTo("12345")
        verify(repository).findByExternalIdPaginated("12345", 0, 10)
    }

    @Test
    fun `should trim search before matching`() {
        val model = MissionModel(
            id = UUID.randomUUID(),
            serviceId = 1,
            externalId = "678",
            startDateTimeUtc = Instant.parse("2025-01-01T00:00:00Z")
        )
        val page = PageImpl(listOf(model), PageRequest.of(0, 10), 1)
        whenever(repository.findByExternalIdPaginated("678", 0, 10)).thenReturn(page)

        val result = getAllMissions.execute(0, 10, "  678  ")

        assertThat(result.content).hasSize(1)
        verify(repository).findByExternalIdPaginated("678", 0, 10)
    }
}
