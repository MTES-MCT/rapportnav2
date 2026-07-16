package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SoftDeleteMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.MissionAdminController
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
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

@WebMvcTest(MissionAdminController::class)
@ContextConfiguration(classes = [MissionAdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class MissionAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getAllMissions: GetAllMissions

    @MockitoBean
    private lateinit var softDeleteMission: SoftDeleteMission

    @Test
    fun `getAll should return paginated missions`() {
        val model = MissionModel(
            id = UUID.randomUUID(),
            serviceId = 1,
            startDateTimeUtc = Instant.parse("2025-01-02T00:00:00Z")
        )
        val page = PageImpl(listOf(model), PageRequest.of(0, 10), 1)
        `when`(getAllMissions.execute(0, 10)).thenReturn(page)

        mockMvc.perform(get("/api/v2/admin/missions?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(1))
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.items.length()").value(1))
            .andExpect(jsonPath("$.items[0].isDeleted").value(false))
    }

    @Test
    fun `getAll should return empty page when no results`() {
        val emptyPage = PageImpl<MissionModel>(emptyList(), PageRequest.of(0, 10), 0)
        `when`(getAllMissions.execute(0, 10)).thenReturn(emptyPage)

        mockMvc.perform(get("/api/v2/admin/missions?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(0))
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.items.length()").value(0))
    }

    @Test
    fun `delete should soft-delete the mission`() {
        val id = UUID.randomUUID()

        mockMvc.perform(delete("/api/v2/admin/missions/$id"))
            .andExpect(status().isOk)

        verify(softDeleteMission).execute(id = id)
    }
}
