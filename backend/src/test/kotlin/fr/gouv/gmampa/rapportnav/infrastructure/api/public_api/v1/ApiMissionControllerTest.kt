package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.v1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.ApiMissionController
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
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

    @MockitoBean
    private lateinit var missionNavRepository: IMissionNavRepository

    private fun missionModel(id: UUID, externalId: Int) = MissionModel(
        id = id,
        externalId = externalId.toString(),
        startDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z")
    )

    @Test
    fun `should return mission with containsActionsAddedByUnit true when nav actions exist`() {
        val externalId = 42
        val missionId = UUID.randomUUID()
        val action = MissionNavActionEntity(
            id = UUID.randomUUID(),
            ownerId = missionId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-01T01:00:00Z")
        )
        `when`(missionNavRepository.findByExternalId(externalId.toString()))
            .thenReturn(Optional.of(missionModel(missionId, externalId)))
        `when`(getComputeNavMission.execute(ownerId = missionId)).thenReturn(listOf(action))

        mockMvc.perform(get("/api/v1/missions/$externalId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(externalId.toString()))
            .andExpect(jsonPath("$.containsActionsAddedByUnit").value(true))

        verify(getComputeNavMission).execute(ownerId = missionId)
    }

    @Test
    fun `should return mission with containsActionsAddedByUnit false when no nav actions`() {
        val externalId = 42
        val missionId = UUID.randomUUID()
        `when`(missionNavRepository.findByExternalId(externalId.toString()))
            .thenReturn(Optional.of(missionModel(missionId, externalId)))
        `when`(getComputeNavMission.execute(ownerId = missionId)).thenReturn(emptyList())

        mockMvc.perform(get("/api/v1/missions/$externalId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(externalId.toString()))
            .andExpect(jsonPath("$.containsActionsAddedByUnit").value(false))
    }

    @Test
    fun `should return mission with containsActionsAddedByUnit false when mission is not found locally`() {
        val externalId = 999
        `when`(missionNavRepository.findByExternalId(externalId.toString())).thenReturn(Optional.empty())

        mockMvc.perform(get("/api/v1/missions/$externalId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(externalId.toString()))
            .andExpect(jsonPath("$.containsActionsAddedByUnit").value(false))
    }

    @Test
    fun `should return 500 when use case throws exception`() {
        val externalId = 42
        val missionId = UUID.randomUUID()
        `when`(missionNavRepository.findByExternalId(externalId.toString()))
            .thenReturn(Optional.of(missionModel(missionId, externalId)))
        `when`(getComputeNavMission.execute(ownerId = missionId))
            .thenThrow(RuntimeException("DB connection failed"))

        mockMvc.perform(get("/api/v1/missions/$externalId"))
            .andExpect(status().isInternalServerError)
    }
}
