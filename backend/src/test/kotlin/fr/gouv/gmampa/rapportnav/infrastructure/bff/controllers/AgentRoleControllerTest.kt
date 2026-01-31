package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentRoles
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.AgentRoleController
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [RapportNavApplication::class])
@WebMvcTest(AgentRoleController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class AgentRoleControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getAgentRoles: GetAgentRoles

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `agentRoles should return list of roles`() {
        val roles = listOf(
            AgentRoleEntity(id = 1, title = "Commandant", priority = 1),
            AgentRoleEntity(id = 2, title = "Second capitaine", priority = 2),
            AgentRoleEntity(id = 3, title = "Mécanicien", priority = 10)
        )

        `when`(getAgentRoles.execute()).thenReturn(roles)

        mockMvc.perform(get("/api/v2/agent_roles"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].title").value("Commandant"))
            .andExpect(jsonPath("$[0].priority").value(1))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].title").value("Second capitaine"))
            .andExpect(jsonPath("$[2].id").value(3))
            .andExpect(jsonPath("$[2].title").value("Mécanicien"))
    }

    @Test
    fun `agentRoles should return empty list when no roles`() {
        `when`(getAgentRoles.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/agent_roles"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }
}
