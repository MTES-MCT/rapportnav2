package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMission2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [GetMissions::class])
class GetMissionsTest {

    @MockitoBean
    private lateinit var getNavMissions: GetNavMissions

    @MockitoBean
    private lateinit var getEnvMissions: GetEnvMissions

    @Autowired
    private lateinit var getMissions: GetMissions

    @MockitoBean
    private lateinit var getControlUnitsForUser: GetControlUnitsForUser

    @MockitoBean
    private lateinit var getMission2: GetMission2


    @Test
    fun `should execute return a list of MissionEntity2`()
    {
        val now = Instant.now()

        val mockEnvMission = MissionEntity(
            id = 1,
            startDateTimeUtc = now,
            endDateTimeUtc = now,
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            controlUnits = listOf(),
            missionTypes = listOf(MissionTypeEnum.AIR),
            hasMissionOrder = false
        )

        val mockNavMission = MissionEntity(
            id = 1,
            startDateTimeUtc = now,
            endDateTimeUtc = now,
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionTypes = listOf(MissionTypeEnum.AIR),
            controlUnits = listOf(),
            hasMissionOrder = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV
        )

        val mockNavMissionEntity2 = MissionEntity2(
            id = 1,
            data = mockNavMission
        )

        val mockEnvMissionEntity2 = MissionEntity2(
            id = 1,
            data = mockEnvMission
        )

        Mockito.`when`(getEnvMissions.execute(
            startedAfterDateTime = now,
            startedBeforeDateTime = now,
            pageNumber = null,
            pageSize = null,
            controlUnits = listOf()
        )).thenReturn(listOf(mockEnvMission))

        Mockito.`when`(getNavMissions.execute(
            startDateTimeUtc = now,
            endDateTimeUtc = now
        )).thenReturn(listOf(mockNavMission))

        Mockito.`when`(getMission2.execute(envMission = mockEnvMission)).thenReturn(mockEnvMissionEntity2)
        Mockito.`when`(getMission2.execute(envMission = mockNavMission)).thenReturn(mockNavMissionEntity2)

        val missions = getMissions.execute(
            startDateTimeUtc = now,
            endDateTimeUtc = now
        )

        Mockito.verify(getEnvMissions).execute(startedAfterDateTime = now, startedBeforeDateTime = now, null, null, listOf())
        Mockito.verify(getNavMissions).execute(startDateTimeUtc = now, endDateTimeUtc = now)

        Assertions.assertEquals(2, missions.size)
    }
}
