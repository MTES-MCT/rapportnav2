package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.v1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.ApiMissionController
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*

@WebMvcTest(ApiMissionController::class)
@ContextConfiguration(classes = [ApiMissionController::class, ControllersExceptionHandler::class])
@AutoConfigureMockMvc(addFilters = false)
class ApiMissionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getComputeNavMission: GetComputeNavActionListByMissionId

    @Test
    fun `should return mission with containsActionsAddedByUnit true when nav actions exist`() {
        val missionId = 42
        val action = MissionNavActionEntity(
            id = UUID.randomUUID(),
            missionId = missionId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-01T01:00:00Z")
        )
        `when`(getComputeNavMission.execute(missionId = missionId)).thenReturn(listOf(action))

        mockMvc.perform(get("/api/v1/missions/$missionId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(missionId))
            .andExpect(jsonPath("$.containsActionsAddedByUnit").value(true))

        verify(getComputeNavMission).execute(missionId = missionId)
    }

    @Test
    fun `should return mission with containsActionsAddedByUnit false when no nav actions`() {
        val missionId = 7
        `when`(getComputeNavMission.execute(missionId = missionId)).thenReturn(emptyList())

        mockMvc.perform(get("/api/v1/missions/$missionId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(missionId))
            .andExpect(jsonPath("$.containsActionsAddedByUnit").value(false))
    }

    @Test
    fun `should return 500 when use case throws exception`() {
        val missionId = 99
        `when`(getComputeNavMission.execute(missionId = missionId))
            .thenThrow(RuntimeException("DB connection failed"))

        mockMvc.perform(get("/api/v1/missions/$missionId"))
            .andExpect(status().isInternalServerError)
    }
}