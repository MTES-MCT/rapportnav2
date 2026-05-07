package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FacadeTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.CreateOrUpdateFishAuction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.DeleteFishAuction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.GetFishAuctions
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.FishAuctionAdminController
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.json.JsonMapper

@WebMvcTest(FishAuctionAdminController::class)
@ContextConfiguration(classes = [FishAuctionAdminController::class])
@AutoConfigureMockMvc(addFilters = false)
class FishAuctionAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getFishAuctions: GetFishAuctions

    @MockitoBean
    private lateinit var createOrUpdateFishAuction: CreateOrUpdateFishAuction

    @MockitoBean
    private lateinit var deleteFishAuction: DeleteFishAuction

    private val jsonMapper = JsonMapper.builder().build()
    private val fishAuctionEntity = FishAuctionEntity(id = 1, name = "Criée de Lorient", facade = FacadeTypeEnum.NAMO)

    @Test
    fun `getFishAuctions should return list`() {
        `when`(getFishAuctions.execute(includeDeleted = true)).thenReturn(listOf(fishAuctionEntity))

        mockMvc.perform(get("/api/v2/admin/fish_auctions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("Criée de Lorient"))
    }

    @Test
    fun `getFishAuctions should return empty list when none`() {
        `when`(getFishAuctions.execute(includeDeleted = true)).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/admin/fish_auctions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `create should return created fish auction`() {
        `when`(createOrUpdateFishAuction.execute(any())).thenReturn(fishAuctionEntity)

        val body = mapOf("id" to null, "name" to "Criée de Lorient", "facade" to "NAMO")

        mockMvc.perform(
            post("/api/v2/admin/fish_auctions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Criée de Lorient"))
    }

    @Test
    fun `create should return null on exception`() {
        `when`(createOrUpdateFishAuction.execute(any())).thenThrow(RuntimeException("error"))

        val body = mapOf("id" to null, "name" to "Criée de Lorient", "facade" to "NAMO")

        mockMvc.perform(
            post("/api/v2/admin/fish_auctions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `update should return updated fish auction`() {
        `when`(createOrUpdateFishAuction.execute(any())).thenReturn(fishAuctionEntity)

        val body = mapOf("id" to 1, "name" to "Criée de Lorient", "facade" to "NAMO")

        mockMvc.perform(
            put("/api/v2/admin/fish_auctions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Criée de Lorient"))
    }

    @Test
    fun `delete should succeed`() {
        mockMvc.perform(delete("/api/v2/admin/fish_auctions/1"))
            .andExpect(status().isOk)

        verify(deleteFishAuction).execute(id = 1)
    }
}