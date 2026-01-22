package fr.gouv.gmampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportModeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionReports
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.ExportBodyRequest
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.MissionExportController
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.json.JsonMapper

@WebMvcTest(MissionExportController::class)
@ContextConfiguration(classes = [MissionExportController::class, JacksonConfig::class, ControllersExceptionHandler::class])
@AutoConfigureMockMvc(addFilters = false)
class MissionExportControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: JsonMapper

    @MockitoBean
    private lateinit var exportMissionReports: ExportMissionReports

    @Test
    fun `should return 200 and export entity for single AEM mission`() {
        val missionIds = listOf(123)
        val exportMode = ExportModeEnum.INDIVIDUAL_MISSION
        val reportType = ExportReportTypeEnum.AEM

        val mockOutput = MissionExportEntity(
            fileName = "Rapport_AEM.ods",
            fileContent = "base64content"
        )

        `when`(exportMissionReports.execute(missionIds, exportMode, reportType)).thenReturn(mockOutput)

        val body = ExportBodyRequest(missionIds, exportMode, reportType)
        val json = objectMapper.writeValueAsString(body)

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.fileName").value("Rapport_AEM.ods"))
            .andExpect(jsonPath("$.fileContent").value("base64content"))

        verify(exportMissionReports, times(1)).execute(missionIds, exportMode, reportType)
    }

    @Test
    fun `should return 200 and export entity for single patrol mission`() {
        val missionIds = listOf(456)
        val exportMode = ExportModeEnum.INDIVIDUAL_MISSION
        val reportType = ExportReportTypeEnum.PATROL

        val mockOutput = MissionExportEntity(
            fileName = "rapport-patrouille.odt",
            fileContent = "base64content"
        )

        `when`(exportMissionReports.execute(missionIds, exportMode, reportType)).thenReturn(mockOutput)

        val body = ExportBodyRequest(missionIds, exportMode, reportType)
        val json = objectMapper.writeValueAsString(body)

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.fileName").value("rapport-patrouille.odt"))

        verify(exportMissionReports, times(1)).execute(missionIds, exportMode, reportType)
    }

    @Test
    fun `should return 200 for combined export mode`() {
        val missionIds = listOf(1, 2, 3)
        val exportMode = ExportModeEnum.COMBINED_MISSIONS_IN_ONE
        val reportType = ExportReportTypeEnum.AEM

        val mockOutput = MissionExportEntity(
            fileName = "tableaux-AEM-combines.ods",
            fileContent = "base64content"
        )

        `when`(exportMissionReports.execute(missionIds, exportMode, reportType)).thenReturn(mockOutput)

        val body = ExportBodyRequest(missionIds, exportMode, reportType)
        val json = objectMapper.writeValueAsString(body)

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.fileName").value("tableaux-AEM-combines.ods"))

        verify(exportMissionReports, times(1)).execute(missionIds, exportMode, reportType)
    }

    @Test
    fun `should return 200 for zipped export mode`() {
        val missionIds = listOf(1, 2)
        val exportMode = ExportModeEnum.MULTIPLE_MISSIONS_ZIPPED
        val reportType = ExportReportTypeEnum.PATROL

        val mockOutput = MissionExportEntity(
            fileName = "rapports-patrouille.zip",
            fileContent = "base64content"
        )

        `when`(exportMissionReports.execute(missionIds, exportMode, reportType)).thenReturn(mockOutput)

        val body = ExportBodyRequest(missionIds, exportMode, reportType)
        val json = objectMapper.writeValueAsString(body)

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.fileName").value("rapports-patrouille.zip"))

        verify(exportMissionReports, times(1)).execute(missionIds, exportMode, reportType)
    }

    @Test
    fun `should deduplicate mission IDs`() {
        val missionIds = listOf(1, 1, 2, 2, 3)
        val expectedDistinctIds = listOf(1, 2, 3)
        val exportMode = ExportModeEnum.COMBINED_MISSIONS_IN_ONE
        val reportType = ExportReportTypeEnum.AEM

        val mockOutput = MissionExportEntity(
            fileName = "export.ods",
            fileContent = "base64content"
        )

        `when`(exportMissionReports.execute(expectedDistinctIds, exportMode, reportType)).thenReturn(mockOutput)

        val body = ExportBodyRequest(missionIds, exportMode, reportType)
        val json = objectMapper.writeValueAsString(body)

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)

        verify(exportMissionReports, times(1)).execute(expectedDistinctIds, exportMode, reportType)
    }

   @Test
    fun `should return 500 when unexpected exception is thrown`() {
        val missionIds = listOf(123)
        val exportMode = ExportModeEnum.INDIVIDUAL_MISSION
        val reportType = ExportReportTypeEnum.AEM

        `when`(exportMissionReports.execute(missionIds, exportMode, reportType))
            .thenThrow(RuntimeException("Unexpected error"))

        val body = ExportBodyRequest(missionIds, exportMode, reportType)
        val json = objectMapper.writeValueAsString(body)

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.message").value("Unexpected error"))
            .andExpect(jsonPath("$.exception").value("RuntimeException"))
    }

    @Test
    fun `should return 500 when missionIds is empty`() {
        val json = """
            {
                "missionIds": [],
                "exportMode": "INDIVIDUAL_MISSION",
                "reportType": "AEM"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isInternalServerError)
    }

    @Test
    fun `should return 500 when exportMode is wrong`() {
        val json = """
            {
                "missionIds": [1],
                "exportMode": "WRONG",
                "reportType": "AEM"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isInternalServerError)
    }

    @Test
    fun `should return 500 when reportType is wrong`() {
        val json = """
            {
                "missionIds": [1],
                "exportMode": "INDIVIDUAL_MISSION",
                "reportType": "WRONG"
            }
        """.trimIndent()

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isInternalServerError)
    }

    @Test
    fun `should return 500 when request body is invalid JSON`() {
        val json = "{ invalid json }"

        mockMvc.perform(
            post("/api/v2/missions/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isInternalServerError)
    }
}
