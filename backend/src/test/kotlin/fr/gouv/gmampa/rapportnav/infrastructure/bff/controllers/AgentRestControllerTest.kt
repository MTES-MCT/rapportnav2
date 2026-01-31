package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId2
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.AgentRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
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
@WebMvcTest(AgentRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class AgentRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getUserFromToken: GetUserFromToken

    @MockitoBean
    private lateinit var getCrewByServiceId2: GetCrewByServiceId2

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `agents should return list of agents for user's service`() {
        val user = UserMock.create(id = 1, serviceId = 10)
        val service = ServiceEntityMock.create(id = 10, name = "Test Service")
        val agents = listOf(
            AgentEntity2(id = 1, firstName = "John", lastName = "Doe", service = service),
            AgentEntity2(id = 2, firstName = "Jane", lastName = "Smith", service = service)
        )

        `when`(getUserFromToken.execute()).thenReturn(user)
        `when`(getCrewByServiceId2.execute(10)).thenReturn(agents)

        mockMvc.perform(get("/api/v2/agents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].firstName").value("John"))
            .andExpect(jsonPath("$[0].lastName").value("Doe"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].firstName").value("Jane"))
            .andExpect(jsonPath("$[1].lastName").value("Smith"))
    }

    @Test
    fun `agents should return empty list when user not found`() {
        `when`(getUserFromToken.execute()).thenReturn(null)

        mockMvc.perform(get("/api/v2/agents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `agents should return empty list when user has no serviceId`() {
        val user = UserMock.create(id = 1, serviceId = null)

        `when`(getUserFromToken.execute()).thenReturn(user)

        mockMvc.perform(get("/api/v2/agents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `agents should return empty list when no agents found for service`() {
        val user = UserMock.create(id = 1, serviceId = 10)

        `when`(getUserFromToken.execute()).thenReturn(user)
        `when`(getCrewByServiceId2.execute(10)).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/agents"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }
}
