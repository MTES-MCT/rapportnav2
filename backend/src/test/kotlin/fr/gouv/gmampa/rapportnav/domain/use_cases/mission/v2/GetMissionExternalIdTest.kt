package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.Optional
import java.util.UUID

@SpringBootTest(classes = [GetMissionExternalId::class])
class GetMissionExternalIdTest {

    @Autowired
    private lateinit var useCase: GetMissionExternalId

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    private fun missionModel(id: UUID, externalId: String?) = MissionModel(
        id = id,
        externalId = externalId,
        startDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z")
    )

    @Test
    fun `returns the Int external id when the mission has a numeric external id`() {
        val missionId = UUID.randomUUID()
        whenever(repository.findById(missionId)).thenReturn(Optional.of(missionModel(missionId, "123")))

        val result = useCase.execute(missionId)

        assertEquals(123, result)
        verify(repository).findById(missionId)
    }

    @Test
    fun `returns null when the mission is not found`() {
        val missionId = UUID.randomUUID()
        whenever(repository.findById(missionId)).thenReturn(Optional.empty())

        assertNull(useCase.execute(missionId))
    }

    @Test
    fun `returns null when the external id is null (nav-only mission)`() {
        val missionId = UUID.randomUUID()
        whenever(repository.findById(missionId)).thenReturn(Optional.of(missionModel(missionId, null)))

        assertNull(useCase.execute(missionId))
    }

    @Test
    fun `returns null when the external id is not numeric`() {
        val missionId = UUID.randomUUID()
        whenever(repository.findById(missionId)).thenReturn(Optional.of(missionModel(missionId, "not-an-int")))

        assertNull(useCase.execute(missionId))
    }
}
