package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.CrewRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
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
@WebMvcTest(CrewRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class CrewRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getCrewByServiceId: GetCrewByServiceId

    @MockitoBean
    private lateinit var getServices: GetServices

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getAllCrews should return list of services with agents`() {
        val service1 = ServiceEntityMock.create(id = 1, name = "Service 1")
        val service2 = ServiceEntityMock.create(id = 2, name = "Service 2")

        val agent1 = AgentEntity(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            service = service1
        )
        val agent2 = AgentEntity(
            id = 2,
            firstName = "Jane",
            lastName = "Smith",
            service = service2
        )

        `when`(getServices.execute()).thenReturn(listOf(service1, service2))
        `when`(getCrewByServiceId.execute(1)).thenReturn(listOf(agent1))
        `when`(getCrewByServiceId.execute(2)).thenReturn(listOf(agent2))

        mockMvc.perform(get("/api/v2/crews"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].service.id").value(1))
            .andExpect(jsonPath("$[0].service.name").value("Service 1"))
            .andExpect(jsonPath("$[0].agents[0].firstName").value("John"))
            .andExpect(jsonPath("$[1].service.id").value(2))
            .andExpect(jsonPath("$[1].service.name").value("Service 2"))
            .andExpect(jsonPath("$[1].agents[0].firstName").value("Jane"))
    }

    @Test
    fun `getAllCrews should return empty list when no services`() {
        `when`(getServices.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/crews"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getAllCrews should return error status when internal exception on getServices`() {
        val internalException = BackendInternalException(
            message = "Database error",
            originalException = RuntimeException("Connection failed")
        )

        `when`(getServices.execute()).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/crews"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }

    @Test
    fun `getAllCrews should return error status when internal exception on getCrewByServiceId`() {
        val service = ServiceEntityMock.create(id = 1, name = "Service 1")
        val internalException = BackendInternalException(
            message = "Database error",
            originalException = RuntimeException("Connection failed")
        )

        `when`(getServices.execute()).thenReturn(listOf(service))
        `when`(getCrewByServiceId.execute(1)).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/crews"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }
}
