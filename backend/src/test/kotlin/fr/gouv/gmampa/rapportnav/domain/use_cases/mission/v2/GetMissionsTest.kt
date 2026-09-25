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
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
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
            serviceId = null,
            navMissionsOnly = true
        )).thenReturn(listOf(navEntity))

        Mockito.`when`(getControlUnitsForUser.execute()).thenReturn(controlUnits)
        Mockito.`when`(getComputeEnvMission.execute(envMission = entity)).thenReturn(response)
        Mockito.`when`(getComputeNavMission.execute(navMission = navEntity)).thenReturn(response)

        val missions = getMissions.execute(
            startDateTimeUtc = now,
            endDateTimeUtc = now
        )

        Mockito.verify(getEnvMissions).execute(startedAfterDateTime = now, startedBeforeDateTime = now, null, null, controlUnits)
        Mockito.verify(getNavMissions).execute(startDateTimeUtc = now, endDateTimeUtc = now, serviceId = null, navMissionsOnly = true)

        Assertions.assertEquals(2, missions.size)
    }

    @Test
    fun `executePaginated windows the merged stream and computes only the page`() {
        // env: 04, 03, 01 ; nav: 02  -> merged desc: envA(04), envB(03), navD(02), envC(01)
        val envA = EnvMissionMock.create(id = 1, startDateTimeUtc = Instant.parse("2025-01-04T00:00:00Z"))
        val envB = EnvMissionMock.create(id = 2, startDateTimeUtc = Instant.parse("2025-01-03T00:00:00Z"))
        val envC = EnvMissionMock.create(id = 3, startDateTimeUtc = Instant.parse("2025-01-01T00:00:00Z"))
        val navD = MissionNavEntity(
            id = UUID.randomUUID(),
            startDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z"),
            isDeleted = false,
            serviceId = 2,
            missionSource = MissionSourceEnum.RAPPORT_NAV
        )

        Mockito.`when`(getUserFromToken.execute()).thenReturn(UserMock.create(serviceId = 2))
        Mockito.`when`(getControlUnitsForUser.execute()).thenReturn(listOf())

        // offset=0, limit=2 -> pageSize = offset + limit + 1 = 3
        Mockito.`when`(getEnvMissions.execute(
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = 0,
            pageSize = 3,
            controlUnits = listOf()
        )).thenReturn(listOf(envA, envB, envC))

        Mockito.`when`(getNavMissions.executeForService(serviceId = 2, startDateTimeUtc = null, endDateTimeUtc = null))
            .thenReturn(listOf(navD))

        // only the windowed page (envA, envB) should be computed
        Mockito.`when`(getComputeEnvMission.execute(envMission = envA)).thenReturn(MissionEntity(id = 1, data = envA))
        Mockito.`when`(getComputeEnvMission.execute(envMission = envB)).thenReturn(MissionEntity(id = 2, data = envB))

        val page = getMissions.executePaginated(offset = 0, limit = 2)

        Assertions.assertEquals(listOf(1, 2), page.missions.map { it.id })
        Assertions.assertTrue(page.hasMore) // 4 raw > offset(0) + limit(2)
        Assertions.assertEquals(2, page.nextOffset)

        // env C (out of window) and nav D (out of window) were NOT computed
        verify(getComputeEnvMission, never()).execute(envMission = envC)
        verify(getComputeNavMission, never()).execute(navMission = navD)
    }

    @Test
    fun `executePaginated reports hasMore false when the window reaches the end`() {
        val envA = EnvMissionMock.create(id = 1, startDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z"))
        val envB = EnvMissionMock.create(id = 2, startDateTimeUtc = Instant.parse("2025-01-01T00:00:00Z"))

        Mockito.`when`(getUserFromToken.execute()).thenReturn(UserMock.create(serviceId = null))
        Mockito.`when`(getControlUnitsForUser.execute()).thenReturn(listOf())
        Mockito.`when`(getEnvMissions.execute(
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = 0,
            pageSize = 16,
            controlUnits = listOf()
        )).thenReturn(listOf(envA, envB))
        Mockito.`when`(getNavMissions.executeForService(serviceId = null, startDateTimeUtc = null, endDateTimeUtc = null))
            .thenReturn(listOf())
        Mockito.`when`(getComputeEnvMission.execute(envMission = envA)).thenReturn(MissionEntity(id = 1, data = envA))
        Mockito.`when`(getComputeEnvMission.execute(envMission = envB)).thenReturn(MissionEntity(id = 2, data = envB))

        val page = getMissions.executePaginated() // offset=0, limit=15 by default

        Assertions.assertEquals(2, page.missions.size)
        Assertions.assertFalse(page.hasMore) // 2 raw <= 15
        Assertions.assertEquals(2, page.nextOffset) // resumes past the 2 examined candidates
    }

    @Test
    fun `executePaginated fills the page with limit matches, skipping non-matching candidates`() {
        // 5 env candidates desc by date; only ids 2, 4, 5 pass the predicate.
        val envA = EnvMissionMock.create(id = 1, startDateTimeUtc = Instant.parse("2025-01-05T00:00:00Z"))
        val envB = EnvMissionMock.create(id = 2, startDateTimeUtc = Instant.parse("2025-01-04T00:00:00Z"))
        val envC = EnvMissionMock.create(id = 3, startDateTimeUtc = Instant.parse("2025-01-03T00:00:00Z"))
        val envD = EnvMissionMock.create(id = 4, startDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z"))
        val envE = EnvMissionMock.create(id = 5, startDateTimeUtc = Instant.parse("2025-01-01T00:00:00Z"))

        Mockito.`when`(getUserFromToken.execute()).thenReturn(UserMock.create(serviceId = null))
        Mockito.`when`(getControlUnitsForUser.execute()).thenReturn(listOf())

        // hasPostFilter = true -> env fetched un-windowed (all candidates in range)
        Mockito.`when`(getEnvMissions.execute(
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = null,
            pageSize = null,
            controlUnits = listOf()
        )).thenReturn(listOf(envA, envB, envC, envD, envE))
        Mockito.`when`(getNavMissions.executeForService(serviceId = null, startDateTimeUtc = null, endDateTimeUtc = null))
            .thenReturn(listOf())
        Mockito.`when`(getComputeEnvMission.execute(envMission = envA)).thenReturn(MissionEntity(id = 1, data = envA))
        Mockito.`when`(getComputeEnvMission.execute(envMission = envB)).thenReturn(MissionEntity(id = 2, data = envB))
        Mockito.`when`(getComputeEnvMission.execute(envMission = envC)).thenReturn(MissionEntity(id = 3, data = envC))
        Mockito.`when`(getComputeEnvMission.execute(envMission = envD)).thenReturn(MissionEntity(id = 4, data = envD))

        val page = getMissions.executePaginated(
            offset = 0,
            limit = 2,
            hasPostFilter = true,
            predicate = { it.id == 2 || it.id == 4 || it.id == 5 }
        )

        // the loop scans past the non-matching id 1 and id 3 to fill the page with 2 matches
        Assertions.assertEquals(listOf(2, 4), page.missions.map { it.id })
        Assertions.assertTrue(page.hasMore) // envE (a further match) still unexamined
        Assertions.assertEquals(4, page.nextOffset) // resumes at the first unexamined raw index
        // it stopped as soon as the page was full — envE was never computed
        verify(getComputeEnvMission, never()).execute(envMission = envE)
    }

    @Test
    fun `executePaginated returns an empty page with hasMore false when the filter matches nothing`() {
        // Regression: an all-filtered-out range must terminate, not report hasMore = true forever.
        val envA = EnvMissionMock.create(id = 1, startDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z"))
        val envB = EnvMissionMock.create(id = 2, startDateTimeUtc = Instant.parse("2025-01-01T00:00:00Z"))

        Mockito.`when`(getUserFromToken.execute()).thenReturn(UserMock.create(serviceId = null))
        Mockito.`when`(getControlUnitsForUser.execute()).thenReturn(listOf())
        Mockito.`when`(getEnvMissions.execute(
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = null,
            pageSize = null,
            controlUnits = listOf()
        )).thenReturn(listOf(envA, envB))
        Mockito.`when`(getNavMissions.executeForService(serviceId = null, startDateTimeUtc = null, endDateTimeUtc = null))
            .thenReturn(listOf())
        Mockito.`when`(getComputeEnvMission.execute(envMission = envA)).thenReturn(MissionEntity(id = 1, data = envA))
        Mockito.`when`(getComputeEnvMission.execute(envMission = envB)).thenReturn(MissionEntity(id = 2, data = envB))

        val page = getMissions.executePaginated(hasPostFilter = true, predicate = { false })

        // env fetched un-windowed so the whole range is scanned in one request
        Mockito.verify(getEnvMissions).execute(
            startedAfterDateTime = null, startedBeforeDateTime = null, pageNumber = null, pageSize = null, controlUnits = listOf()
        )
        Assertions.assertTrue(page.missions.isEmpty())
        Assertions.assertFalse(page.hasMore) // stream exhausted -> no endless "load more"
        Assertions.assertEquals(2, page.nextOffset)
    }
}
