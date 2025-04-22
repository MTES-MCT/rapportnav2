package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionPatrolCombined
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionPatrolSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportMissionPatrolCombined::class, FormatDateTime::class])
class ExportMissionPatrolCombinedTest {

    @Autowired
    private lateinit var exportMissionPatrolCombined: ExportMissionPatrolCombined

    @MockitoBean
    private lateinit var exportMissionPatrolSingle: ExportMissionPatrolSingle

    @MockitoBean
    private lateinit var getMission: GetMission

    @Test
    fun `should return null for empty mission list`() {
        val result = exportMissionPatrolCombined.execute(emptyList())
        assertEquals(null, result)
    }

    @Test
    fun `should export a file`() {
        val missionIds = listOf("1")
        val mission = MissionEntityMock.create(id = missionIds.first())
        Mockito.`when`(exportMissionPatrolSingle.createFile(Mockito.any())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionPatrolSingle.odt",
                fileContent = "MockContent"
            )
        )
        Mockito.`when`(getMission.execute(Mockito.anyString(), Mockito.any())).thenReturn(
            mission
        )

        val result = exportMissionPatrolCombined.execute(missionIds)

        assertNotNull(result)
        assertEquals("rapports-patrouille-combinés_2022-01-02.odt", result?.fileName)
    }

    @Test
    fun `should handle exception and return null`() {
        // Arrange: Force an exception when getMission.execute is called
        val missionIds = listOf("1")
        Mockito.`when`(getMission.execute(Mockito.anyString(), Mockito.any()))
            .thenThrow(RuntimeException("Mock exception"))

        // Act: Call the method
        val result = exportMissionPatrolCombined.execute(missionIds)

        // Assert: Verify the result is null and no further interactions happen
        assertEquals(null, result)
        Mockito.verifyNoInteractions(exportMissionPatrolSingle)
    }

}
