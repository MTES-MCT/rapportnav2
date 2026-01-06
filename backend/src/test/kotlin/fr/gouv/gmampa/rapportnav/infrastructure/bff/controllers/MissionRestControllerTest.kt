package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.MissionRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.LegacyControlUnitEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Instant

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [RapportNavApplication::class])
@WebMvcTest(MissionRestController::class)
class MissionRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var getServiceForUser: GetServiceForUser

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    @MockitoBean
    private lateinit var getComputeNavMission: GetComputeNavMission

    @MockitoBean
    private lateinit var createMission: CreateMission

    @MockitoBean
    private lateinit var getMissions: GetMissions

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var deleteMissionNav: DeleteMissionNav

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    /**
     *
     *  private val getComputeEnvMission: GetComputeEnvMission,
     *     private val getServiceForUser: GetServiceForUser,
     *     private val createMission: CreateMission,
     *     private val getMissions: GetMissions,
     *     private val getComputeNavMission: GetComputeNavMission
     */


    @Test
    fun `should return a list of missions`() {
        // Arrange
        val mockMissionEntity = MissionEntityMock2.create(id = 1)
        `when`(getMissions.execute(Instant.parse("2025-04-16T09:02:58.082289Z"))).thenReturn(listOf(mockMissionEntity))

        // Act & Assert
        mockMvc.perform(
            get("/api/v2/missions")
                .param("startDateTimeUtc", "2025-04-16T09:02:58.082289Z")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
    }

    @Test
    fun `should return a mission by id`() {
        // Arrange
        val missionId = 1
        val mockMissionEntity = MissionEntityMock2.create(id = 1)
        `when`(getComputeEnvMission.execute(missionId = 1)).thenReturn(mockMissionEntity)

        // Act & Assert
        mockMvc.perform(get("/api/v2/missions/{missionId}", missionId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(missionId))
    }

    @Test
    fun `should create a new mission`() {
        val controlUnitsIds = listOf(123)
        // Arrange
        val service = ServiceEntityMock.create(2, name = "test", controlUnits = controlUnitsIds)
        val requestBody = MissionGeneralInfo2Mock.create()
        val mockMission = MissionEntityMock2.create(
            id = 123,
            controlUnits = listOf(LegacyControlUnitEntityMock.create(id = controlUnitsIds.first()))
        )
        val json = objectMapper.writeValueAsString(requestBody)
        `when`(getServiceForUser.execute()).thenReturn(service)
        `when`(createMission.execute(requestBody, service)).thenReturn(mockMission)

        // Act & Assert
        mockMvc.perform(
            post("/api/v2/missions")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(123))
    }

    @Test
    fun `should return null when mission creation fails`() {
        // Arrange
        val requestBody = MissionGeneralInfo2Mock.create()
        val json = objectMapper.writeValueAsString(requestBody)
        val service = ServiceEntityMock.create(2, name = "test", controlUnits = listOf(123))
        `when`(getServiceForUser.execute()).thenReturn(service)
        // Simulate mission creation returning null
        `when`(
            createMission.execute(
                requestBody,
                service
            )
        ).thenThrow(RuntimeException("Mission creation failed"))
        // Act & Assert
        mockMvc.perform(
            post("/api/v2/missions")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(json)
        )
            .andExpect(status().isOk)  // The status should be 200 OK
            .andExpect(
                content().string("")
            )
    }


}
