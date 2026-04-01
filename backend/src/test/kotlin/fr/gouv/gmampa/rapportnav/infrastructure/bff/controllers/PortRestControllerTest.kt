package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetPorts
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.PortRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.PortEntityMock
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
@WebMvcTest(PortRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class PortRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getPorts: GetPorts

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getPorts should return filtered and sorted list of ports`() {
        val filteredPorts = listOf(
            PortEntityMock.create(locode = "FRLEH", name = "Le Havre"),
            PortEntityMock.create(locode = "FRLER", name = "Le Rayon")
        )

        `when`(getPorts.execute(name = "Le")).thenReturn(filteredPorts)

        mockMvc.perform(get("/api/v2/ports").param("search", "Le"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].locode").value("FRLEH"))
            .andExpect(jsonPath("$[0].name").value("Le Havre"))
            .andExpect(jsonPath("$[1].locode").value("FRLER"))
            .andExpect(jsonPath("$[1].name").value("Le Rayon"))
            .andExpect(jsonPath("$[2]").doesNotExist())
    }

    @Test
    fun `getPorts should return empty list when no match`() {
        `when`(getPorts.execute(name = "Zeta")).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/ports").param("search", "Zeta"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getPorts should return error status when internal exception`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        `when`(getPorts.execute(name = "Le")).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/ports").param("search", "Le"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }
}
