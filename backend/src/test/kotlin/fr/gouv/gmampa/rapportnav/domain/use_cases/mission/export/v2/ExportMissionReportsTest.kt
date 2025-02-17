package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportModeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [ExportMissionReports::class])
class ExportMissionReportsTest {

    @Autowired
    private lateinit var exportMissionReports: ExportMissionReports

    @MockitoBean
    private lateinit var exportMissionPatrolSingle: ExportMissionPatrolSingle

    @MockitoBean
    private lateinit var exportMissionPatrolCombined: ExportMissionPatrolCombined

    @MockitoBean
    private lateinit var exportMissionPatrolMultipleZipped: ExportMissionPatrolMultipleZipped

    @MockitoBean
    private lateinit var exportMissionAEMSingle: ExportMissionAEMSingle

    @MockitoBean
    private lateinit var exportMissionAEMCombined: ExportMissionAEMCombined

    @MockitoBean
    private lateinit var exportMissionAEMMultipleZipped: ExportMissionAEMMultipleZipped

    @MockitoBean
    private lateinit var exportMissionAEMSingle2: ExportMissionAEMSingle2

    @BeforeEach
    fun setUp() {
        Mockito.`when`(exportMissionPatrolSingle.execute(Mockito.anyInt())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionPatrolSingle.odt",
                fileContent = "MockContent"
            )
        )
        Mockito.`when`(exportMissionPatrolCombined.execute(Mockito.anyList())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionPatrolCombined.odt",
                fileContent = "MockContent"
            )
        )
        Mockito.`when`(exportMissionPatrolMultipleZipped.execute(Mockito.anyList())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionPatrolMultipleZipped.zip",
                fileContent = "MockContent"
            )
        )
        Mockito.`when`(exportMissionAEMSingle.execute(Mockito.anyInt())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionAEMSingle.ods",
                fileContent = "MockContent"
            )
        )
        Mockito.`when`(exportMissionAEMCombined.execute(Mockito.anyList())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionAEMCombined.ods",
                fileContent = "MockContent"
            )
        )
        Mockito.`when`(exportMissionAEMMultipleZipped.execute(Mockito.anyList())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionAEMMultipleZipped.zip",
                fileContent = "MockContent"
            )
        )

        Mockito.`when`(exportMissionAEMSingle2.execute(Mockito.anyList())).thenReturn(
            MissionExportEntity(
                fileName = "exportMissionAEMMultipleZipped.zip",
                fileContent = "MockContent"
            )
        )
    }

    @Test
    fun `should return null for empty mission list`() {
        val result = exportMissionReports.execute(
            emptyList(),
            ExportModeEnum.INDIVIDUAL_MISSION,
            ExportReportTypeEnum.AEM
        )
        assertEquals(null, result)
    }

    @Test
    fun `should export Patrol single mission`() {
        val missionIds = listOf(1)
        val result = exportMissionReports.execute(
            missionIds,
            ExportModeEnum.INDIVIDUAL_MISSION,
            ExportReportTypeEnum.PATROL
        )

        assertNotNull(result)
        assertEquals("exportMissionPatrolSingle.odt", result?.fileName)
    }

    @Test
    fun `should export AEM single mission`() {
        val missionIds = listOf(1)
        val result = exportMissionReports.execute(
            missionIds,
            ExportModeEnum.INDIVIDUAL_MISSION,
            ExportReportTypeEnum.AEM
        )

        assertNotNull(result)
        assertEquals("exportMissionAEMSingle.ods", result?.fileName)
    }

    @Test
    fun `should export Patrol combined missions`() {
        val missionIds = listOf(1, 2, 3)
        val result = exportMissionReports.execute(
            missionIds,
            ExportModeEnum.COMBINED_MISSIONS_IN_ONE,
            ExportReportTypeEnum.PATROL
        )

        assertNotNull(result)
        assertEquals("exportMissionPatrolCombined.odt", result?.fileName)
    }

    @Test
    fun `should export AEM combined missions`() {
        val missionIds = listOf(1, 2, 3)
        val result = exportMissionReports.execute(
            missionIds,
            ExportModeEnum.COMBINED_MISSIONS_IN_ONE,
            ExportReportTypeEnum.AEM
        )

        assertNotNull(result)
        assertEquals("exportMissionAEMCombined.ods", result?.fileName)
    }

    @Test
    fun `should export Patrol multiple missions zipped`() {
        val missionIds = listOf(1, 2, 3)
        val result = exportMissionReports.execute(
            missionIds,
            ExportModeEnum.MULTIPLE_MISSIONS_ZIPPED,
            ExportReportTypeEnum.PATROL
        )

        assertNotNull(result)
        assertEquals("exportMissionPatrolMultipleZipped.zip", result?.fileName)
    }

    @Test
    fun `should export AEM multiple missions zipped`() {
        val missionIds = listOf(1, 2, 3)
        val result = exportMissionReports.execute(
            missionIds,
            ExportModeEnum.MULTIPLE_MISSIONS_ZIPPED,
            ExportReportTypeEnum.AEM
        )

        assertNotNull(result)
        assertEquals("exportMissionAEMMultipleZipped.zip", result?.fileName)
    }
}
