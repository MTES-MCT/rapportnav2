package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api

import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.VersionController
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(VersionController::class)
@ContextConfiguration(classes = [VersionController::class])
@AutoConfigureMockMvc(addFilters = false)
class VersionControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var buildProperties: BuildProperties

    @Test
    fun `version should return version and commit hash`() {
        `when`(buildProperties.version).thenReturn("2.79.3")
        `when`(buildProperties["commit.hash"]).thenReturn("abc123")

        mockMvc.perform(get("/version"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.version").value("2.79.3"))
            .andExpect(jsonPath("$.commit").value("abc123"))
    }

    @Test
    fun `version should handle null commit hash`() {
        `when`(buildProperties.version).thenReturn("2.79.3")
        `when`(buildProperties["commit.hash"]).thenReturn(null)

        mockMvc.perform(get("/version"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.version").value("2.79.3"))
            .andExpect(jsonPath("$.commit").isEmpty)
    }
}