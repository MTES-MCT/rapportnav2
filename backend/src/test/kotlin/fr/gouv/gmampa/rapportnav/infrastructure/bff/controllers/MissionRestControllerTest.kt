package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import java.util.*
import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.MissionRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.LegacyControlUnitEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import tools.jackson.databind.json.JsonMapper
import java.time.Instant

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [RapportNavApplication::class, JacksonConfig::class, ControllersExceptionHandler::class])
@WebMvcTest(MissionRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class MissionRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: JsonMapper

    @MockitoBean
    private lateinit var getServiceForUser: GetServiceForUser

    @MockitoBean
    private lateinit var createMission: CreateMission

    @MockitoBean
    private lateinit var getMissions: GetMissions

    @MockitoBean
    private lateinit var getComputeMission: GetComputeMission

    @MockitoBean
    private lateinit var deleteMission: DeleteMission

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `should return a list of missions`() {
        val mockMissionEntity = MissionEntityMock.create()
        `when`(getMissions.execute(Instant.parse("2025-04-16T09:02:58.082289Z"))).thenReturn(listOf(mockMissionEntity))

        mockMvc.perform(
            get("/api/v2/missions")
                .param("startDateTimeUtc", "2025-04-16T09:02:58.082289Z")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
    }

    @Test
    fun `should return a mission by id`() {
        val missionId = UUID.randomUUID()
        val mockMissionEntity = MissionEntityMock.create(id = missionId)
        `when`(getComputeMission.execute(id = missionId)).thenReturn(mockMissionEntity)

        mockMvc.perform(get("/api/v2/missions/{missionId}", missionId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(missionId.toString()))
    }

    @Test
    fun `should return error for invalid missionId format`() {
        mockMvc.perform(get("/api/v2/missions/{missionId}", "not-a-uuid"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should create a new mission`() {
        val controlUnitsIds = listOf(123)
        val missionId = UUID.randomUUID()
        val service = ServiceEntityMock.create(2, name = "test", controlUnits = controlUnitsIds)
        val requestBody = MissionGeneralInfo2Mock.create()
        val mockMission = MissionEntityMock.create(
            id = missionId,
            controlUnits = listOf(LegacyControlUnitEntityMock.create(id = controlUnitsIds.first()))
        )
        val json = objectMapper.writeValueAsString(requestBody)
        `when`(getServiceForUser.execute()).thenReturn(service)
        `when`(createMission.execute(requestBody, service)).thenReturn(mockMission)

        mockMvc.perform(
            post("/api/v2/missions")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(missionId.toString()))
    }

    @Test
    fun `should return error when mission creation fails with validation error`() {
        val requestBody = MissionGeneralInfo2Mock.create()
        val json = objectMapper.writeValueAsString(requestBody)
        val service = ServiceEntityMock.create(2, name = "test", controlUnits = listOf(123))
        `when`(getServiceForUser.execute()).thenReturn(service)
        whenever(createMission.execute(any(), any())).thenAnswer {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "CreateMission: Control Units or Service are falsy"
            )
        }

        mockMvc.perform(
            post("/api/v2/missions")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(json)
        )
            .andExpect(status().isBadRequest)
    }
}
