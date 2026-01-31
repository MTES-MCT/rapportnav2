package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.ControlUnitResourceRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ControlUnitResourceEnvMock
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
@WebMvcTest(ControlUnitResourceRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class ControlUnitResourceRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getControlUnitResources: GetControlUnitResources

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getAll should return list of control unit resources`() {
        val resources = listOf(
            ControlUnitResourceEnvMock.create(id = 1, name = "Resource 1"),
            ControlUnitResourceEnvMock.create(id = 2, name = "Resource 2")
        )

        `when`(getControlUnitResources.execute()).thenReturn(resources)

        mockMvc.perform(get("/api/v2/resources"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Resource 1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Resource 2"))
    }

    @Test
    fun `getAll should return empty list when no resources`() {
        `when`(getControlUnitResources.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/resources"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getAll should return error status when internal exception`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        `when`(getControlUnitResources.execute()).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/resources"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }
}
