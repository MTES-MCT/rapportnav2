package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FacadeTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.GetFishAuctions
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.FishAuctionController
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
@WebMvcTest(FishAuctionController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class FishAuctionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getFishAuctions: GetFishAuctions

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getFishAuctions should return list of auctions`() {
        val auctions = listOf(
            FishAuctionEntity(id = 1, name = "Boulogne-sur-Mer", facade = FacadeTypeEnum.MEMN),
            FishAuctionEntity(id = 2, name = "Saint-Malo", facade = FacadeTypeEnum.NAMO),
            FishAuctionEntity(id = 3, name = "La Rochelle", facade = FacadeTypeEnum.SA)
        )

        `when`(getFishAuctions.execute()).thenReturn(auctions)

        mockMvc.perform(get("/api/v2/fish_auctions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Boulogne-sur-Mer"))
            .andExpect(jsonPath("$[0].facade").value("MEMN"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Saint-Malo"))
            .andExpect(jsonPath("$[1].facade").value("NAMO"))
            .andExpect(jsonPath("$[2].facade").value("SA"))
    }

    @Test
    fun `getFishAuctions should return empty list when no auctions`() {
        `when`(getFishAuctions.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/fish_auctions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }
}
