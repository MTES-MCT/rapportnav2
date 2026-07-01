package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetCountries
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.CountryRestController
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ApiKeyAuthenticationFilter
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
@WebMvcTest(CountryRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class CountryRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getCountries: GetCountries

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getCountries should return list of mapped countries`() {
        `when`(getCountries.execute()).thenReturn(
            listOf(
                CountryEntity(iso2 = "FR", iso3 = "FRA", name = "France", flag = "🇫🇷"),
                CountryEntity(iso2 = "DE", iso3 = "DEU", name = "Allemagne", flag = "🇩🇪"),
            )
        )

        mockMvc.perform(get("/api/v2/countries"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].iso2").value("FR"))
            .andExpect(jsonPath("$[0].iso3").value("FRA"))
            .andExpect(jsonPath("$[0].name").value("France"))
            .andExpect(jsonPath("$[0].flag").value("🇫🇷"))
            .andExpect(jsonPath("$[1].iso2").value("DE"))
            .andExpect(jsonPath("$[1].iso3").value("DEU"))
    }

    @Test
    fun `getCountries should return empty list when no countries`() {
        `when`(getCountries.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/countries"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `getCountries should return error status when internal exception`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )
        `when`(getCountries.execute()).thenAnswer { throw internalException }

        mockMvc.perform(get("/api/v2/countries"))
            .andExpect { result -> assert(result.response.status >= 400) }
    }
}
