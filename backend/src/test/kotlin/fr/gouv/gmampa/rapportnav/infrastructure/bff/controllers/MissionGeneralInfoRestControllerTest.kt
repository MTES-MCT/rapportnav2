package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.MissionGeneralInfoRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.GeneralInfoEntityMock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [RapportNavApplication::class])
@WebMvcTest(MissionGeneralInfoRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class MissionGeneralInfoRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateGeneralInfo: UpdateGeneralInfo

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `update should return updated general info for integer missionId`() {
        val missionId = 123
        val entity = GeneralInfoEntityMock.create(missionId = missionId)
        val result = MissionGeneralInfoEntity(
            data = entity,
            crew = emptyList(),
            passengers = emptyList()
        )

        `when`(updateGeneralInfo.execute(missionId = eq(missionId), generalInfo = anyOrNull())).thenReturn(result)

        mockMvc.perform(
            put("/api/v2/missions/$missionId/general_infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"missionId": $missionId}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.missionId").value(missionId))
    }

    @Test
    fun `update should return updated general info for UUID missionId`() {
        val missionIdUUID = UUID.randomUUID()
        val entity = GeneralInfoEntityMock.create(missionIdUUID = missionIdUUID)
        val result = MissionGeneralInfoEntity(
            data = entity,
            crew = emptyList(),
            passengers = emptyList()
        )

        `when`(updateGeneralInfo.execute(missionIdUUID = eq(missionIdUUID), generalInfo = anyOrNull())).thenReturn(result)

        mockMvc.perform(
            put("/api/v2/missions/$missionIdUUID/general_infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"missionIdUUID": "$missionIdUUID"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.missionIdUUID").value(missionIdUUID.toString()))
    }

}
