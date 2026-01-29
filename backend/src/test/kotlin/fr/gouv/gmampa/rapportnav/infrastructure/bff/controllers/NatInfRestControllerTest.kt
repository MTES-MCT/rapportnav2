package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetNatinfs
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.NatInfRestController
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
@WebMvcTest(NatInfRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class NatInfRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getNatinfs: GetNatinfs

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getNatinfs should return list of natinfs`() {
        val natinfs = listOf(
            NatinfEntity(infraction = "Infraction 1", natinfCode = 111),
            NatinfEntity(infraction = "Infraction 2", natinfCode = 222)
        )

        `when`(getNatinfs.execute()).thenReturn(natinfs)

        mockMvc.perform(get("/api/v2/natinfs"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].natinfCode").value(111))
            .andExpect(jsonPath("$[0].infraction").value("Infraction 1"))
            .andExpect(jsonPath("$[1].natinfCode").value(222))
            .andExpect(jsonPath("$[1].infraction").value("Infraction 2"))
    }

    @Test
    fun `getNatinfs should return empty list when no natinfs`() {
        `when`(getNatinfs.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/natinfs"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getNatinfs should return error status when internal exception`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        `when`(getNatinfs.execute()).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/natinfs"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }
}
