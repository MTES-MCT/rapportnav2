package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.PatchNavMission
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [PatchNavMission::class])
class PatchNavMissionTest {

    @Autowired
    private lateinit var patchNavMission: PatchNavMission

    @MockitoBean
    private lateinit var getNavMissionById2: GetNavMissionById2

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Test
    fun `execute should return null when MissionEntity are equal`() {
        val missionIdUUID = UUID.randomUUID()

        // Given
        val entity = MissionNavEntity(
            serviceId = 2,
            isDeleted = false,
            id = missionIdUUID,
            observationsByUnit = "observation",
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00")
        )

        val input = MissionNavInputEntity(
            isDeleted = true,
            observationsByUnit = "myObservations",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00")
        )

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(repository.save(anyOrNull())).thenReturn(entity.toMissionModel())
        Mockito.`when`(getNavMissionById2.execute(id = missionIdUUID)).thenReturn(entity)

        // When
        patchNavMission.execute(id = missionIdUUID, input)

        // Then
        verify(repository, times(1)).save(entity.fromMissionNavInput(input))
    }

    @Test
    fun `execute should return MissionEnvEntity when MissionEntity are not equal`() {
        val missionIdUUID = UUID.randomUUID()

        // Given
        val entity = MissionNavEntity(
            serviceId = 2,
            isDeleted = false,
            id = missionIdUUID,
            observationsByUnit = "observation",
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00")
        )

        val input = MissionNavInputEntity(
            isDeleted = false,
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00")
        )

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(repository.save(anyOrNull())).thenReturn(entity.toMissionModel())
        Mockito.`when`(getNavMissionById2.execute(id = missionIdUUID)).thenReturn(entity)

        // When
        patchNavMission.execute(id = missionIdUUID, input)

        // Then
        verify(repository, never()).save(entity.fromMissionNavInput(input))
    }
}
