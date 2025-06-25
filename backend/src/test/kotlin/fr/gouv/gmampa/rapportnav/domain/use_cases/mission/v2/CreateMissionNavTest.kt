package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateMissionNav
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
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
            id= UUID.randomUUID(),
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
}
