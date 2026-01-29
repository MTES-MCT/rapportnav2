package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.VesselRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.VesselEntityMock
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
@WebMvcTest(VesselRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class VesselRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getVessels: GetVessels

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getVessels should return filtered list of vessels`() {
        val vessels = listOf(
            VesselEntityMock.create(vesselId = 1, vesselName = "Alpha Ship"),
            VesselEntityMock.create(vesselId = 2, vesselName = "Alpha Boat"),
            VesselEntityMock.create(vesselId = 3, vesselName = "Beta Ship")
        )

        `when`(getVessels.execute()).thenReturn(vessels)

        mockMvc.perform(get("/api/v2/vessels").param("search", "Alpha"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].vesselId").value(2))
            .andExpect(jsonPath("$[0].vesselName").value("Alpha Boat"))
            .andExpect(jsonPath("$[1].vesselId").value(1))
            .andExpect(jsonPath("$[1].vesselName").value("Alpha Ship"))
            .andExpect(jsonPath("$[2]").doesNotExist())
    }

    @Test
    fun `getVessels should return empty list when no match`() {
        val vessels = listOf(
            VesselEntityMock.create(vesselId = 1, vesselName = "Alpha Ship")
        )

        `when`(getVessels.execute()).thenReturn(vessels)

        mockMvc.perform(get("/api/v2/vessels").param("search", "Zeta"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getVessels should return error status when internal exception`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        `when`(getVessels.execute()).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/vessels").param("search", "Alpha"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }

    @Test
    fun `getVesselById should return vessel when found`() {
        val vessels = listOf(
            VesselEntityMock.create(vesselId = 1, vesselName = "Alpha Ship"),
            VesselEntityMock.create(vesselId = 2, vesselName = "Beta Ship")
        )

        `when`(getVessels.execute()).thenReturn(vessels)

        mockMvc.perform(get("/api/v2/vessels/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.vesselId").value(1))
            .andExpect(jsonPath("$.vesselName").value("Alpha Ship"))
    }

    @Test
    fun `getVesselById should return null when not found`() {
        val vessels = listOf(
            VesselEntityMock.create(vesselId = 1, vesselName = "Alpha Ship")
        )

        `when`(getVessels.execute()).thenReturn(vessels)

        mockMvc.perform(get("/api/v2/vessels/999"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `getVesselById should return error status when internal exception`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        `when`(getVessels.execute()).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/vessels/1"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }
}
