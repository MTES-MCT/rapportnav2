package fr.gouv.gmampa.rapportnav.infrastructure.api.analytics

import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.ApiAnalyticsController
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.MissionIdsRequest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.analytics.ComputeAEMData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.analytics.ComputePatrolData
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.adapters.output.ApiAnalyticsAEMDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.adapters.output.ApiAnalyticsPatrolDataOutput
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ApiAnalyticsController::class)
@ContextConfiguration(classes = [ApiAnalyticsController::class])
@AutoConfigureMockMvc(addFilters = false)
class ApiAnalyticsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var computeAEMData: ComputeAEMData

    @MockitoBean
    private lateinit var computePatrolData: ComputePatrolData

    // --- AEM TESTS ----------------------------------------------------------

    @Test
    fun `should return 200 and computed AEM data`() {
        val missionIds = listOf("1", "2")
        val mockOutput1 = ApiAnalyticsAEMDataOutput(/* fill with fake values */)
        val mockOutput2 = ApiAnalyticsAEMDataOutput(/* fill with fake values */)

        `when`(computeAEMData.execute(1)).thenReturn(mockOutput1)
        `when`(computeAEMData.execute(2)).thenReturn(mockOutput2)

        val body = MissionIdsRequest(missionIds)
        val json = objectMapper.writeValueAsString(body)

        mockMvc.perform(
            post("/api/analytics/v1/aem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.count").value(2))
            .andExpect(jsonPath("$.failedIds").isEmpty)
            .andExpect(jsonPath("$.results").isArray)
            .andExpect(jsonPath("$.results.length()").value(2))

        verify(computeAEMData, times(1)).execute(1)
        verify(computeAEMData, times(1)).execute(2)
    }

    @Test
    fun `should return 400 when all missionIds are invalid`() {
        val missionIds = listOf("abc", "xyz")
        val json = objectMapper.writeValueAsString(MissionIdsRequest(missionIds))

        mockMvc.perform(
            post("/api/analytics/v1/aem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("No valid mission IDs provided"))
    }

    @Test
    fun `should include failedIds when one AEM computation fails`() {
        val missionIds = listOf("1", "2")
        val mockOutput = ApiAnalyticsAEMDataOutput()

        `when`(computeAEMData.execute(1)).thenReturn(mockOutput)
        `when`(computeAEMData.execute(2)).thenThrow(RuntimeException("Computation failed"))

        val json = objectMapper.writeValueAsString(MissionIdsRequest(missionIds))

        mockMvc.perform(
            post("/api/analytics/v1/aem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.failedIds[0]").value("2"))
    }

    // --- PATROL TESTS -------------------------------------------------------

    @Test
    fun `should return 200 and computed Patrol data`() {
        val missionIds = listOf("3")
        val mockOutput = ApiAnalyticsPatrolDataOutput()

        `when`(computePatrolData.execute(3)).thenReturn(mockOutput)

        val json = objectMapper.writeValueAsString(MissionIdsRequest(missionIds))

        mockMvc.perform(
            post("/api/analytics/v1/patrol")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.count").value(1))
            .andExpect(jsonPath("$.failedIds").isEmpty)
            .andExpect(jsonPath("$.results[0]").exists())

        verify(computePatrolData).execute(3)
    }

    @Test
    fun `should handle patrol computation exception gracefully`() {
        val missionIds = listOf("3")
        `when`(computePatrolData.execute(3)).thenThrow(RuntimeException("Boom"))

        val json = objectMapper.writeValueAsString(MissionIdsRequest(missionIds))

        mockMvc.perform(
            post("/api/analytics/v1/patrol")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.count").value(0))
            .andExpect(jsonPath("$.failedIds[0]").value("3"))
    }
}
