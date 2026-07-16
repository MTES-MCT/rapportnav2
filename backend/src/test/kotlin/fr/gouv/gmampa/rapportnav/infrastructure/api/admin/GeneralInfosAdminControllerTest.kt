package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import java.util.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteGeneralInfos
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllGeneralInfos
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfoAdmin
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.GeneralInfosAdminController
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.json.JsonMapper

@WebMvcTest(GeneralInfosAdminController::class)
@ContextConfiguration(classes = [GeneralInfosAdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class GeneralInfosAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateGeneralInfo: UpdateGeneralInfoAdmin

    @MockitoBean
    private lateinit var getAllGeneralInfos: GetAllGeneralInfos

    @MockitoBean
    private lateinit var deleteGeneralInfos: DeleteGeneralInfos

    private val jsonMapper = JsonMapper.builder().build()

    @Test
    fun `getAll should return paginated general infos`() {
        val model = MissionGeneralInfoModel(id = 1, )
        val page = PageImpl(listOf(model), PageRequest.of(0, 10), 1)
        `when`(getAllGeneralInfos.execute(0, 10, null)).thenReturn(page)

        mockMvc.perform(get("/api/v2/admin/general-infos?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(1))
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.items.length()").value(1))
    }

    @Test
    fun `getAll should return empty page when no results`() {
        val emptyPage = PageImpl<MissionGeneralInfoModel>(emptyList(), PageRequest.of(0, 10), 0)
        `when`(getAllGeneralInfos.execute(0, 10, null)).thenReturn(emptyPage)

        mockMvc.perform(get("/api/v2/admin/general-infos?page=0&size=10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalItems").value(0))
            .andExpect(jsonPath("$.items").isArray)
            .andExpect(jsonPath("$.items.length()").value(0))
    }

    @Test
    fun `getAll should pass search parameter`() {
        val emptyPage = PageImpl<MissionGeneralInfoModel>(emptyList(), PageRequest.of(0, 10), 0)
        `when`(getAllGeneralInfos.execute(0, 10, "search-term")).thenReturn(emptyPage)

        mockMvc.perform(get("/api/v2/admin/general-infos?page=0&size=10&search=search-term"))
            .andExpect(status().isOk)

        verify(getAllGeneralInfos).execute(0, 10, "search-term")
    }

    @Test
    fun `update should return updated general info`() {
        val missionId = UUID.randomUUID()
        val entity = MissionGeneralInfoEntity(id = 1, distanceInNauticalMiles = 42.5f)
        `when`(updateGeneralInfo.execute(any())).thenReturn(entity)

        val body = mapOf("id" to 1, "missionId" to missionId.toString(), "distanceInNauticalMiles" to 42.5)

        mockMvc.perform(
            put("/api/v2/admin/general-infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.distanceInNauticalMiles").value(42.5))
    }

    @Test
    fun `update should return null when not found`() {
        `when`(updateGeneralInfo.execute(any())).thenReturn(null)

        val body = mapOf("id" to 999, "missionId" to UUID.randomUUID().toString())

        mockMvc.perform(
            put("/api/v2/admin/general-infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `delete should succeed`() {
        mockMvc.perform(delete("/api/v2/admin/general-infos/1"))
            .andExpect(status().isOk)

        verify(deleteGeneralInfos).execute(id = 1)
    }
}
