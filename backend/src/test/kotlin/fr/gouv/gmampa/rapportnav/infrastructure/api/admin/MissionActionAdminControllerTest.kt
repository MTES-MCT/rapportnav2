package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllMissionActions
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.MissionActionAdminController
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.UUID

@WebMvcTest(MissionActionAdminController::class)
@ContextConfiguration(classes = [MissionActionAdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class MissionActionAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getAllMissionActions: GetAllMissionActions

    private fun action(id: UUID = UUID.randomUUID(), ownerId: UUID = UUID.randomUUID()): MissionActionModel =
        MissionActionModel(
            id = id,
            ownerId = ownerId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z")
        )

    @Test
    fun `getAll should return paginated mission actions`() {
        val model = action(ownerId = UUID.randomUUID())
        val page = PageImpl(listOf(model), PageRequest.of(0, 10), 1)
        `when`(getAllMissionActions.execute(0, 10, null, null)).thenReturn(page)

        mockMvc.perform(get("/api/v2/admin/mission-actions?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(1))
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.items.length()").value(1))
            .andExpect(jsonPath("$.items[0].id").value(model.id.toString()))
            .andExpect(jsonPath("$.items[0].actionType").value("CONTROL"))
    }

    @Test
    fun `getAll should return empty page when no results`() {
        val emptyPage = PageImpl<MissionActionModel>(emptyList(), PageRequest.of(0, 10), 0)
        `when`(getAllMissionActions.execute(0, 10, null, null)).thenReturn(emptyPage)

        mockMvc.perform(get("/api/v2/admin/mission-actions?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(0))
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.items.length()").value(0))
    }

    @Test
    fun `getAll should pass searchId parameter through`() {
        val id = UUID.randomUUID()
        val emptyPage = PageImpl<MissionActionModel>(emptyList(), PageRequest.of(0, 10), 0)
        `when`(getAllMissionActions.execute(0, 10, id.toString(), null)).thenReturn(emptyPage)

        mockMvc.perform(get("/api/v2/admin/mission-actions?page=0&size=10&searchId=$id"))
            .andExpect(status().isOk)

        verify(getAllMissionActions).execute(0, 10, id.toString(), null)
    }

    @Test
    fun `getAll should pass searchOwnerId parameter through`() {
        val ownerId = UUID.randomUUID()
        val emptyPage = PageImpl<MissionActionModel>(emptyList(), PageRequest.of(0, 10), 0)
        `when`(getAllMissionActions.execute(0, 10, null, ownerId.toString())).thenReturn(emptyPage)

        mockMvc.perform(get("/api/v2/admin/mission-actions?page=0&size=10&searchOwnerId=$ownerId"))
            .andExpect(status().isOk)

        verify(getAllMissionActions).execute(0, 10, null, ownerId.toString())
    }
}
