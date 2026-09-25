package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissions
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [GetNavMissions::class])
class GetNavMissionsTest {

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Autowired
    private lateinit var getNavMissions: GetNavMissions

    val mockMission = MissionModel(
        id = UUID.randomUUID(),
        startDateTimeUtc = Instant.now(),
        endDateTimeUtc = Instant.now(),
        isDeleted = false,
        missionSource = MissionSourceEnum.RAPPORT_NAV,
        serviceId = 2
    )
    val mockMissionWithDifferentService = MissionModel(
        id = UUID.randomUUID(),
        startDateTimeUtc = Instant.now(),
        endDateTimeUtc = Instant.now(),
        isDeleted = false,
        missionSource = MissionSourceEnum.RAPPORT_NAV,
        serviceId = 3
    )

    @Test
    fun `should execute retrieve missions as list of MissionEntity2`()
    {
        Mockito.`when`(repository.findAll(
            startBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z")
        )).thenReturn(listOf(mockMission, mockMissionWithDifferentService))

        val missions = getNavMissions.execute(
            startDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            serviceId = 2
        )

        Assertions.assertNotNull(missions)
        Assertions.assertNotNull(missions?.get(0))
        Assertions.assertEquals(1, missions?.size)
        assertThat(missions?.get(0)).isInstanceOf(MissionNavEntity::class.java)
    }

    @Test
    fun `should execute retrieve all missions if serviceId is null`()
    {
        Mockito.`when`(repository.findAll(
            startBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z")
        )).thenReturn(listOf(mockMission, mockMissionWithDifferentService))

        val missions = getNavMissions.execute(
            startDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            serviceId = null
        )

        Assertions.assertNotNull(missions)
        Assertions.assertEquals(2, missions?.size)
    }

    @Test
    fun `should exclude only missions whose externalId is an Int when navMissionsOnly is true`()
    {
        val envMirror = MissionModel(
            id = UUID.randomUUID(),
            externalId = "123", // MonitorEnv Int reference -> excluded
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            isDeleted = false,
            missionSource = MissionSourceEnum.MONITORENV,
            serviceId = null
        )
        val nonIntExternalId = MissionModel(
            id = UUID.randomUUID(),
            externalId = "not-an-int", // not a MonitorEnv reference -> kept
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            isDeleted = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            serviceId = null
        )
        Mockito.`when`(repository.findAll(
            startBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endBeforeDateTime = Instant.parse("2025-04-07T09:23:00.912559Z")
        )).thenReturn(listOf(mockMission, envMirror, nonIntExternalId))

        val missions = getNavMissions.execute(
            startDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            serviceId = null,
            navMissionsOnly = true
        )

        Assertions.assertEquals(2, missions?.size)
        assertThat(missions?.mapNotNull { it.externalId }).containsExactly("not-an-int")
    }

    @Test
    fun `execute should use a default end date one month ahead when endDateTimeUtc is null`()
    {
        Mockito.`when`(repository.findAll(
            startBeforeDateTime = eq(Instant.parse("2025-04-07T09:23:00.912559Z")),
            endBeforeDateTime = any()
        )).thenReturn(listOf(mockMission))

        val missions = getNavMissions.execute(
            startDateTimeUtc = Instant.parse("2025-04-07T09:23:00.912559Z"),
            endDateTimeUtc = null
        )

        Assertions.assertEquals(1, missions?.size)
    }

    @Test
    fun `executeForService should return empty list and skip the repository when serviceId is null`()
    {
        val missions = getNavMissions.executeForService(serviceId = null)

        assertThat(missions).isEmpty()
        Mockito.verifyNoInteractions(repository)
    }

    @Test
    fun `executeForService should map nav missions and drop env-mirror rows whose externalId is an Int`()
    {
        val envMirror = MissionModel(
            id = UUID.randomUUID(),
            externalId = "456", // MonitorEnv Int reference -> excluded
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            isDeleted = false,
            missionSource = MissionSourceEnum.MONITORENV,
            serviceId = 2
        )
        Mockito.`when`(repository.findNavMissionsForService(
            serviceId = eq(2),
            startedAfter = any(),
            startedBefore = any()
        )).thenReturn(listOf(mockMission, envMirror))

        val missions = getNavMissions.executeForService(serviceId = 2)

        Assertions.assertEquals(1, missions.size)
        assertThat(missions[0]).isInstanceOf(MissionNavEntity::class.java)
    }

    @Test
    fun `executeForService should default to wide date bounds when no dates are provided`()
    {
        Mockito.`when`(repository.findNavMissionsForService(
            serviceId = eq(2),
            startedAfter = eq(Instant.EPOCH),
            startedBefore = eq(Instant.parse("9999-12-31T23:59:59Z"))
        )).thenReturn(listOf(mockMission))

        val missions = getNavMissions.executeForService(serviceId = 2)

        Assertions.assertEquals(1, missions.size)
    }

    @Test
    fun `executeForService should pass through the explicit date bounds when provided`()
    {
        val start = Instant.parse("2025-01-01T00:00:00Z")
        val end = Instant.parse("2025-02-01T00:00:00Z")
        Mockito.`when`(repository.findNavMissionsForService(
            serviceId = eq(2),
            startedAfter = eq(start),
            startedBefore = eq(end)
        )).thenReturn(listOf(mockMission))

        val missions = getNavMissions.executeForService(
            serviceId = 2,
            startDateTimeUtc = start,
            endDateTimeUtc = end
        )

        Assertions.assertEquals(1, missions.size)
    }
}
