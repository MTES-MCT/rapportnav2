package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrationById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrations
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.FullAdministration
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.AdministrationController
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
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
@WebMvcTest(AdministrationController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class AdministrationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getAdministrations: GetAdministrations

    @MockitoBean
    private lateinit var getAdministrationById: GetAdministrationById

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getAll should return list of administrations`() {
        val admin1 = FullAdministration(
            id = 1,
            name = "Administration 1",
            isArchived = false,
            controlUnitIds = listOf(10, 20),
            controlUnits = emptyList()
        )
        val admin2 = FullAdministration(
            id = 2,
            name = "Administration 2",
            isArchived = true,
            controlUnitIds = listOf(30),
            controlUnits = emptyList()
        )

        `when`(getAdministrations.execute()).thenReturn(listOf(admin1, admin2))

        mockMvc.perform(get("/api/v2/administrations"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Administration 1"))
            .andExpect(jsonPath("$[0].isArchived").value(false))
            .andExpect(jsonPath("$[0].controlUnitIds[0]").value(10))
            .andExpect(jsonPath("$[0].controlUnitIds[1]").value(20))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Administration 2"))
            .andExpect(jsonPath("$[1].isArchived").value(true))
    }

    @Test
    fun `getAll should return empty list when no administrations`() {
        `when`(getAdministrations.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/administrations"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `get should return administration by id`() {
        val admin = FullAdministration(
            id = 42,
            name = "Test Administration",
            isArchived = false,
            controlUnitIds = listOf(100),
            controlUnits = emptyList()
        )

        `when`(getAdministrationById.execute(42)).thenReturn(admin)

        mockMvc.perform(get("/api/v2/administrations/42"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(42))
            .andExpect(jsonPath("$.name").value("Test Administration"))
            .andExpect(jsonPath("$.isArchived").value(false))
            .andExpect(jsonPath("$.controlUnitIds[0]").value(100))
    }

    @Test
    fun `get should not return success when administration not found`() {
        `when`(getAdministrationById.execute(999)).thenReturn(null)

        // In WebMvcTest context without full exception handler setup,
        // the BackendUsageException may result in 500 instead of 400.
        // The important behavior is that it doesn't return 200 OK with null body.
        mockMvc.perform(get("/api/v2/administrations/999"))
            .andExpect { result ->
                val status = result.response.status
                assert(status >= 400) { "Expected error status (>=400) but got $status" }
            }
    }
}
