package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateMissionNav
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [CreateMissionNav::class])
class CreateMissionNavTest {

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Autowired
    private lateinit var createMissionNav: CreateMissionNav

    @Test
    fun `should execute and return an MissionNavEntity`() {
        val generalInfo2 = MissionGeneralInfo2(
            missionTypes = listOf(MissionTypeEnum.AIR),
            missionReportType = MissionReportTypeEnum.OFFICE_REPORT,
            isMissionArmed = false,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now()
        )

        val controlUnitIds = listOf(1, 2)

        val navMission = MissionNavEntity(
            controlUnits = controlUnitIds,
            startDateTimeUtc = generalInfo2.startDateTimeUtc!!,
            endDateTimeUtc = generalInfo2.endDateTimeUtc,
            isDeleted = false,
            controlUnitIdOwner = controlUnitIds.first()
        )

        val model = MissionModel(
            id = 1,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            controlUnitIdOwner = 1,
            isDeleted = false,
            startDateTimeUtc = generalInfo2.startDateTimeUtc!!,
            endDateTimeUtc = generalInfo2.endDateTimeUtc,
            controlUnits = listOf(1, 2)
        )

        Mockito.`when`(repository.save(navMission)).thenReturn(model)

        val result = createMissionNav.execute(generalInfo2 = generalInfo2, controlUnitIds = controlUnitIds)


        assertNotNull(result)

        assertEquals(1, result?.id)
        assertEquals(1, result?.controlUnitIdOwner)
    }
}
