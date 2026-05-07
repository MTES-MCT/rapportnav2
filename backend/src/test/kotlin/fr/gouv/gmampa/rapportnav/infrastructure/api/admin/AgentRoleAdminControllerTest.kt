package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgentRole
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteAgentRole
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.AgentRoleAdminController
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.json.JsonMapper

@WebMvcTest(AgentRoleAdminController::class)
@ContextConfiguration(classes = [AgentRoleAdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class AgentRoleAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var createOrUpdateAgentRole: CreateOrUpdateAgentRole

    @MockitoBean
    private lateinit var deleteAgentRole: DeleteAgentRole

    private val jsonMapper = JsonMapper.builder().build()
    private val roleEntity = AgentRoleEntity(id = 1, title = "Captain", priority = 1)

    @Test
    fun `create should return created agent role`() {
        `when`(createOrUpdateAgentRole.execute(any())).thenReturn(roleEntity)

        val body = mapOf("id" to null, "title" to "Captain", "priority" to 1)

        mockMvc.perform(
            post("/api/v2/admin/agent_roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Captain"))
            .andExpect(jsonPath("$.priority").value(1))
    }

    @Test
    fun `create should return null on exception`() {
        `when`(createOrUpdateAgentRole.execute(any())).thenThrow(RuntimeException("error"))

        val body = mapOf("id" to null, "title" to "Captain", "priority" to 1)

        mockMvc.perform(
            post("/api/v2/admin/agent_roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `update should return updated agent role`() {
        `when`(createOrUpdateAgentRole.execute(any())).thenReturn(roleEntity)

        val body = mapOf("id" to 1, "title" to "Captain", "priority" to 1)

        mockMvc.perform(
            put("/api/v2/admin/agent_roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Captain"))
    }

    @Test
    fun `delete should succeed`() {
        mockMvc.perform(delete("/api/v2/admin/agent_roles/1"))
            .andExpect(status().isOk)

        verify(deleteAgentRole).execute(id = 1)
    }
}