package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindById
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.UserRestController
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
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
@WebMvcTest(UserRestController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class UserRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var findById: FindById

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    // Mock the TokenService that your SecurityConfig depends on
    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @Test
    fun `getUserById should return user info when user exists`() {
        val user = UserMock.create(id = 1, email = "test@example.com", firstName = "john", lastName = "doe", serviceId = 10)
        val service = ServiceEntityMock.create(id = 10, name = "PAM Jeanne Barret A", controlUnits = listOf(100, 200))

        `when`(findById.execute(1)).thenReturn(user)
        `when`(getServiceById.execute(10)).thenReturn(service)

        mockMvc.perform(get("/api/v2/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.firstName").value("john"))
            .andExpect(jsonPath("$.lastName").value("doe"))
            .andExpect(jsonPath("$.serviceId").value(10))
            .andExpect(jsonPath("$.serviceName").value("PAM Jeanne Barret A"))
            .andExpect(jsonPath("$.controlUnitId").value(100))
    }


    @Test
    fun `getUserById should return user info with null service fields when service not found`() {
        val user = UserMock.create(id = 1, email = "test@example.com", serviceId = 999)

        `when`(findById.execute(1)).thenReturn(user)
        `when`(getServiceById.execute(999)).thenReturn(null)

        mockMvc.perform(get("/api/v2/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.serviceId").doesNotExist())
            .andExpect(jsonPath("$.serviceName").doesNotExist())
            .andExpect(jsonPath("$.controlUnitId").doesNotExist())
    }

    @Test
    fun `getUserById should return user info with null controlUnitId when service has no control units`() {
        val user = UserMock.create(id = 1, serviceId = 10)
        val service = ServiceEntityMock.create(id = 10, name = "Test Service", controlUnits = emptyList())

        `when`(findById.execute(1)).thenReturn(user)
        `when`(getServiceById.execute(10)).thenReturn(service)

        mockMvc.perform(get("/api/v2/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.serviceId").value(10))
            .andExpect(jsonPath("$.serviceName").value("Test Service"))
            .andExpect(jsonPath("$.controlUnitId").doesNotExist())
    }

    @Test
    fun `getUserById should return user info when user has no serviceId`() {
        val user = UserMock.create(id = 1, email = "test@example.com", serviceId = null)

        `when`(findById.execute(1)).thenReturn(user)
        `when`(getServiceById.execute(null)).thenReturn(null)

        mockMvc.perform(get("/api/v2/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.serviceId").doesNotExist())
            .andExpect(jsonPath("$.serviceName").doesNotExist())
    }
}
