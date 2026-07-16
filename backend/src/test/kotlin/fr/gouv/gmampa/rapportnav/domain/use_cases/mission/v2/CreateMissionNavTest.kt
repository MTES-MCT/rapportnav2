package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateMissionNav
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [CreateMissionNav::class])
class CreateMissionNavTest {

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Autowired
    private lateinit var createMissionNav: CreateMissionNav

    @Test
    fun `should throw exception when startDateTimeUtc is null`() {
        val generalInfo2 = MissionGeneralInfo2(
            missionTypes = listOf(MissionTypeEnum.AIR),
            missionReportType = MissionReportTypeEnum.OFFICE_REPORT,
            startDateTimeUtc = null,
            endDateTimeUtc = null
        )

        val exception = assertThrows<BackendUsageException> {
            createMissionNav.execute(generalInfo2 = generalInfo2, serviceId = 1)
        }

        assertEquals(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION, exception.code)
        assertEquals("CreateMissionNav: startDateTimeUtc is required", exception.message)
    }

    @Test
    fun `should execute and return an MissionNavEntity`() {
        val serviceId = 2
        val generalInfo2 = MissionGeneralInfo2(
            missionTypes = listOf(MissionTypeEnum.AIR),
            missionReportType = MissionReportTypeEnum.OFFICE_REPORT,
            isMissionArmed = false,
            startDateTimeUtc = Instant.parse("2025-04-24T14:13:17.022715Z"),
            endDateTimeUtc = Instant.parse("2025-04-24T14:13:17.022715Z")
        )
        val navMission = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = serviceId,
            startDateTimeUtc = generalInfo2.startDateTimeUtc!!,
            endDateTimeUtc = generalInfo2.endDateTimeUtc,
            isDeleted = false
        )

        val missionModel = navMission.toMissionModel()
        Mockito.`when`(repository.save(anyOrNull())).thenReturn(missionModel)

        val result = createMissionNav.execute(generalInfo2 = generalInfo2, serviceId = serviceId)

        assertNotNull(result)
        Mockito.verify(repository, times(1)).save(anyOrNull())
    }

    @Test
    fun `should return entity with correct serviceId and dates`() {
        val serviceId = 5
        val startDate = Instant.parse("2025-06-15T08:00:00Z")
        val endDate = Instant.parse("2025-06-15T18:00:00Z")
        val generalInfo2 = MissionGeneralInfo2(
            missionTypes = listOf(MissionTypeEnum.SEA),
            missionReportType = MissionReportTypeEnum.OFFICE_REPORT,
            startDateTimeUtc = startDate,
            endDateTimeUtc = endDate
        )

        val missionId = UUID.randomUUID()
        val navMission = MissionNavEntity(
            id = missionId,
            serviceId = serviceId,
            startDateTimeUtc = startDate,
            endDateTimeUtc = endDate,
            isDeleted = false
        )

        Mockito.`when`(repository.save(anyOrNull())).thenReturn(navMission.toMissionModel())

        val result = createMissionNav.execute(generalInfo2 = generalInfo2, serviceId = serviceId)

        assertEquals(serviceId, result.serviceId)
        assertEquals(startDate, result.startDateTimeUtc)
        assertEquals(endDate, result.endDateTimeUtc)
        assertEquals(false, result.isDeleted)
    }

    @Test
    fun `should create mission with null endDateTimeUtc`() {
        val serviceId = 3
        val startDate = Instant.parse("2025-07-01T10:00:00Z")
        val generalInfo2 = MissionGeneralInfo2(
            missionTypes = listOf(MissionTypeEnum.LAND),
            missionReportType = MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT,
            startDateTimeUtc = startDate,
            endDateTimeUtc = null
        )

        val navMission = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = serviceId,
            startDateTimeUtc = startDate,
            endDateTimeUtc = null,
            isDeleted = false
        )

        Mockito.`when`(repository.save(anyOrNull())).thenReturn(navMission.toMissionModel())

        val result = createMissionNav.execute(generalInfo2 = generalInfo2, serviceId = serviceId)

        assertNotNull(result)
        assertEquals(null, result.endDateTimeUtc)
    }

    @Test
    fun `should create nav mission from MissionEntity with externalId`() {
        val missionId = UUID.randomUUID()
        val externalId = "21916"
        val startDate = Instant.parse("2025-08-01T09:00:00Z")
        val endDate = Instant.parse("2025-08-01T17:00:00Z")

        val mission = fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity(
            id = missionId,
            externalId = externalId,
            data = fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity(
                externalId = 21916,
                startDateTimeUtc = startDate,
                endDateTimeUtc = endDate,
                missionSource = fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum.MONITORENV,
                isDeleted = false
            )
        )

        val expectedModel = MissionNavEntity(
            id = missionId,
            externalId = externalId,
            startDateTimeUtc = startDate,
            endDateTimeUtc = endDate,
            missionSource = fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum.MONITORENV,
            isDeleted = false
        ).toMissionModel()

        Mockito.`when`(repository.save(anyOrNull())).thenReturn(expectedModel)

        val result = createMissionNav.execute(mission = mission)

        assertNotNull(result)
        assertEquals(missionId, result.id)
        assertEquals(externalId, result.externalId)
        Mockito.verify(repository, times(1)).save(anyOrNull())
    }

    @Test
    fun `should create nav mission from MissionEntity without externalId`() {
        val missionId = UUID.randomUUID()
        val startDate = Instant.parse("2025-08-01T09:00:00Z")

        val mission = fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity(
            id = missionId,
            data = fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity(
                startDateTimeUtc = startDate,
                missionSource = fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum.RAPPORT_NAV,
                isDeleted = false
            )
        )

        val expectedModel = MissionNavEntity(
            id = missionId,
            startDateTimeUtc = startDate,
            isDeleted = false
        ).toMissionModel()

        Mockito.`when`(repository.save(anyOrNull())).thenReturn(expectedModel)

        val result = createMissionNav.execute(mission = mission)

        assertNotNull(result)
        assertEquals(missionId, result.id)
        assertEquals(null, result.externalId)
        Mockito.verify(repository, times(1)).save(anyOrNull())
    }
}
