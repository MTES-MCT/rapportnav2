package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.AgentAdminController
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
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

@WebMvcTest(AgentAdminController::class)
@ContextConfiguration(classes = [AgentAdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class AgentAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getAgent: GetAllAgent

    @MockitoBean
    private lateinit var deleteAgent: DeleteAgent

    @MockitoBean
    private lateinit var disableAgent: DisableAgent

    @MockitoBean
    private lateinit var migrateAgent: MigrateAgent

    @MockitoBean
    private lateinit var createOrUpdateAgent: CreateOrUpdateAgent

    private val jsonMapper = JsonMapper.builder().build()

    private val serviceEntity = ServiceEntity(id = 1, name = "PAM Jeanne Barret", serviceType = ServiceTypeEnum.PAM)
    private val agentEntity = AgentEntity(id = 1, firstName = "John", lastName = "Doe", service = serviceEntity)

    @Test
    fun `agents should return list of agents`() {
        `when`(getAgent.execute()).thenReturn(listOf(agentEntity))

        mockMvc.perform(get("/api/v2/admin/agents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].firstName").value("John"))
    }

    @Test
    fun `agents should return empty list on exception`() {
        `when`(getAgent.execute()).thenThrow(RuntimeException("DB error"))

        mockMvc.perform(get("/api/v2/admin/agents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `create should return created agent`() {
        val input = AgentInput2(firstName = "Jane", lastName = "Doe", serviceId = 1)
        `when`(createOrUpdateAgent.execute(any())).thenReturn(agentEntity)

        mockMvc.perform(
            post("/api/v2/admin/agents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value("John"))
    }

    @Test
    fun `create should return null on exception`() {
        val input = AgentInput2(firstName = "Jane", lastName = "Doe", serviceId = 1)
        `when`(createOrUpdateAgent.execute(any())).thenThrow(RuntimeException("error"))

        mockMvc.perform(
            post("/api/v2/admin/agents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `update should return updated agent`() {
        val input = AgentInput2(id = 1, firstName = "Jane", lastName = "Updated", serviceId = 1)
        `when`(createOrUpdateAgent.execute(any())).thenReturn(agentEntity)

        mockMvc.perform(
            put("/api/v2/admin/agents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value("John"))
    }

    @Test
    fun `migrate should return migrated agent`() {
        val input = AgentInput2(id = 1, firstName = "John", lastName = "Doe", serviceId = 2)
        `when`(migrateAgent.execute(any())).thenReturn(agentEntity)

        mockMvc.perform(
            post("/api/v2/admin/agents/1/migrate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value("John"))
    }

    @Test
    fun `migrate should return null on exception`() {
        val input = AgentInput2(id = 1, firstName = "John", lastName = "Doe", serviceId = 2)
        `when`(migrateAgent.execute(any())).thenThrow(RuntimeException("error"))

        mockMvc.perform(
            post("/api/v2/admin/agents/1/migrate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `disable should succeed`() {
        mockMvc.perform(post("/api/v2/admin/agents/1/disable"))
            .andExpect(status().isOk)

        verify(disableAgent).execute(1)
    }

    @Test
    fun `delete should succeed`() {
        mockMvc.perform(delete("/api/v2/admin/agents/1"))
            .andExpect(status().isOk)

        verify(deleteAgent).execute(id = 1)
    }
}
