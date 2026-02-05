package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeNavMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetMissions::class])
class GetMissionsTest {

    @MockitoBean
    private lateinit var getNavMissions: GetNavMissions

    @MockitoBean
    private lateinit var getEnvMissions: GetEnvMissions

    @Autowired
    private lateinit var getMissions: GetMissions

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @MockitoBean
    private lateinit var getComputeNavMission: GetComputeNavMission

    @MockitoBean
    private lateinit var getControlUnitsForUser: GetControlUnitsForUser

    @MockitoBean
    private lateinit var getUserFromToken: GetUserFromToken


    @Test
    fun `should execute return a list of MissionEntity2`()
    {

        val now = Instant.now()
        val controlUnits = listOf<Int>()

        val entity = MissionEnvEntity(
            id = 1,
            startDateTimeUtc = now,
            endDateTimeUtc = now,
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            controlUnits = listOf(),
            missionTypes = listOf(MissionTypeEnum.AIR),
            hasMissionOrder = false,
        )

        val navEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            startDateTimeUtc = now,
            endDateTimeUtc = now,
            isDeleted = false,
            serviceId = 2,
            missionSource = MissionSourceEnum.RAPPORT_NAV
        )

        val response = MissionEntity(
            id = 1,
            data = entity
        )

        Mockito.`when`(getUserFromToken.execute()).thenReturn(UserMock.create(serviceId = null))

        Mockito.`when`(getEnvMissions.execute(
            startedAfterDateTime = now,
            startedBeforeDateTime = now,
            pageNumber = null,
            pageSize = null,
            controlUnits = listOf()
        )).thenReturn(listOf(entity))

        Mockito.`when`(getNavMissions.execute(
            startDateTimeUtc = now,
            endDateTimeUtc = now,
            serviceId = null
        )).thenReturn(listOf(navEntity))

        Mockito.`when`(getControlUnitsForUser.execute()).thenReturn(controlUnits)
        Mockito.`when`(getComputeEnvMission.execute(envMission = entity)).thenReturn(response)
        Mockito.`when`(getComputeNavMission.execute(navMission = navEntity)).thenReturn(response)

        val missions = getMissions.execute(
            startDateTimeUtc = now,
            endDateTimeUtc = now
        )

        Mockito.verify(getEnvMissions).execute(startedAfterDateTime = now, startedBeforeDateTime = now, null, null, controlUnits)
        Mockito.verify(getNavMissions).execute(startDateTimeUtc = now, endDateTimeUtc = now)

        Assertions.assertEquals(2, missions.size)
    }
}
