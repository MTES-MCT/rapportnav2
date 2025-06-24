package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateMissionNav
import fr.gouv.gmampa.rapportnav.mocks.mission.LegacyControlUnitEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [CreateMission::class])
class CreateMissionTest {

    @Autowired
    private lateinit var createMission: CreateMission

    @MockitoBean
    private lateinit var createMissionNav: CreateMissionNav

    @MockitoBean
    private lateinit var createEnvMission: CreateEnvMission

    @Test
    fun`should execute and create a RapportNav mission when is an reinforcement type`(){
        val serviceId = 2
       val  generalInfo2 = MissionGeneralInfo2Mock.create(
           missionReportType = MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT
       )

        val mockMissionNav = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            startDateTimeUtc = Instant.now(),
            isDeleted = false
        )

        Mockito.`when`(createMissionNav.execute(generalInfo2, serviceId = serviceId)).thenReturn(mockMissionNav)
        createMission.execute(
            generalInfo2 = generalInfo2,
            service = ServiceEntity(
                id = serviceId,
                name = "Iris B",
                controlUnits = listOf(1,2)
            )
        )

        Mockito.verify(createMissionNav, Mockito.times(1)).execute(generalInfo2, serviceId = 2)
        Mockito.verify(createEnvMission, Mockito.never()).execute(generalInfo2, controlUnitIds = listOf(1))
    }

    @Test
    fun`should execute and create a RapportNav mission when is an office type`(){
        val serviceId = 2
        val  generalInfo2 = MissionGeneralInfo2Mock.create(
            missionReportType = MissionReportTypeEnum.OFFICE_REPORT
        )

        val mockMissionNav = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            startDateTimeUtc = Instant.now(),
            isDeleted = false
        )

        Mockito.`when`(createMissionNav.execute(generalInfo2, serviceId = 2)).thenReturn(mockMissionNav)

        createMission.execute(
            generalInfo2 = generalInfo2,
            service = ServiceEntity(
                id = serviceId,
                name = "Iris B",
                controlUnits = listOf(1,2)
            )
        )

        Mockito.verify(createMissionNav, Mockito.times(1)).execute(generalInfo2, serviceId = 2)
        Mockito.verify(createEnvMission, Mockito.never()).execute(generalInfo2, controlUnitIds = listOf(1))
    }

    @Test
    fun`should execute and create a MonitorEnv mission when is a field type`(){
        val serviceId = 2
        val  generalInfo2 = MissionGeneralInfo2Mock.create(
            missionReportType = MissionReportTypeEnum.FIELD_REPORT
        )

        val mockMissionEnv = MissionEnvEntity(
            id = 1,
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            controlUnits = listOf(LegacyControlUnitEntityMock.create()),
            missionTypes = listOf(MissionTypeEnum.AIR),
            missionSource = MissionSourceEnum.RAPPORT_NAV

        )

        val mockMissionNav = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            startDateTimeUtc = Instant.now(),
            isDeleted = false
        )

        Mockito.`when`(createMissionNav.execute(generalInfo2, serviceId = serviceId)).thenReturn(mockMissionNav)
        Mockito.`when`(createEnvMission.execute(generalInfo2, controlUnitIds = listOf(1,2))).thenReturn(mockMissionEnv)

        createMission.execute(
            generalInfo2 = generalInfo2,
            service = ServiceEntity(
                id = serviceId,
                name = "Iris B",
                controlUnits = listOf(1,2)
            )
        )

        Mockito.verify(createMissionNav, Mockito.never()).execute(generalInfo2, serviceId = serviceId)
        Mockito.verify(createEnvMission, Mockito.times(1)).execute(generalInfo2, controlUnitIds = listOf(1, 2))
    }

}
