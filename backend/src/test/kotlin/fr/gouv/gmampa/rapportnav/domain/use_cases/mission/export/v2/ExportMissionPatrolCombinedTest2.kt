package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionPatrolCombined2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionPatrolSingle2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMission2
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportMissionPatrolCombined2::class, FormatDateTime::class])
class ExportMissionPatrolCombinedTest2 {

    @Autowired
    private lateinit var exportMissionPatrolCombined: ExportMissionPatrolCombined2

    @MockitoBean
    private lateinit var exportMissionPatrolSingle: ExportMissionPatrolSingle2

    @MockitoBean
    private lateinit var getMission2: GetMission2

    @Test
    fun `should return null for empty mission list`() {
        val result = exportMissionPatrolCombined.execute(emptyList())
        assertEquals(null, result)
    }

    @Test
    fun `should export a file`() {
        val missionIds = listOf("1")
        val mission2 = MissionEntityMock2.create(
            generalInfos = MissionGeneralInfoEntity2(
                data = MissionGeneralInfoEntity(
                    id = 1,
                    missionId = missionIds.first(),
                    nbrOfRecognizedVessel = 3,
                    consumedFuelInLiters = 3f,
                    consumedGOInLiters = 3f,
                    distanceInNauticalMiles = 3f
                ),
                crew = listOf(MissionCrewEntityMock.create()),
            )
        )

        Mockito.`when`(getMission2.execute(missionId = mission2.id)).thenReturn(
            mission2
        )

        val result = exportMissionPatrolCombined.execute(missionIds)

        assertNotNull(result)
        assertEquals("rapports-patrouille-combinés_2022-01-02.odt", result?.fileName)
    }

    @Test
    fun `should handle exception and return null`() {
        // Arrange: Force an exception when getMission.execute is called
        val missionIds = listOf("1")
        Mockito.`when`(getMission2.execute(missionId = "1"))
            .thenThrow(RuntimeException("Mock exception"))

        // Act: Call the method
        val result = exportMissionPatrolCombined.execute(missionIds)

        // Assert: Verify the result is null and no further interactions happen
        assertEquals(null, result)
        Mockito.verifyNoInteractions(exportMissionPatrolSingle)
    }

}
