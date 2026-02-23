package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.ProcessImpersonationRequest
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ServiceAdminController
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
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
import java.time.Instant

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [RapportNavApplication::class])
@WebMvcTest(ServiceAdminController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class ServiceAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getServices: GetServices

    @MockitoBean
    private lateinit var deleteService: DeleteService

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @MockitoBean
    private lateinit var createOrUpdateService: CreateOrUpdateService

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @MockitoBean
    private lateinit var processImpersonationRequest: ProcessImpersonationRequest

    @Test
    fun `getServices should return all services when active param not provided`() {
        val services = listOf(
            ServiceEntityMock.create(id = 1, name = "Service 1", serviceType = ServiceTypeEnum.PAM),
            ServiceEntityMock.create(id = 2, name = "Service 2", serviceType = ServiceTypeEnum.ULAM, deletedAt = Instant.now())
        )

        `when`(getServices.execute()).thenReturn(services)

        mockMvc.perform(get("/api/v2/admin/services"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Service 1"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Service 2"))
    }

    @Test
    fun `getServices should return only active services when active=true`() {
        val activeService = ServiceEntityMock.create(id = 1, name = "Active Service", serviceType = ServiceTypeEnum.PAM)
        val deletedService = ServiceEntityMock.create(id = 2, name = "Deleted Service", serviceType = ServiceTypeEnum.ULAM, deletedAt = Instant.now())
        val services = listOf(activeService, deletedService)

        `when`(getServices.execute()).thenReturn(services)

        mockMvc.perform(get("/api/v2/admin/services").param("active", "true"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Active Service"))
    }

    @Test
    fun `getServices should return all services when active=false`() {
        val activeService = ServiceEntityMock.create(id = 1, name = "Active Service", serviceType = ServiceTypeEnum.PAM)
        val deletedService = ServiceEntityMock.create(id = 2, name = "Deleted Service", serviceType = ServiceTypeEnum.ULAM, deletedAt = Instant.now())
        val services = listOf(activeService, deletedService)

        `when`(getServices.execute()).thenReturn(services)

        mockMvc.perform(get("/api/v2/admin/services").param("active", "false"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
    }

    @Test
    fun `getServices should return empty list when no active services and active=true`() {
        val deletedService = ServiceEntityMock.create(id = 1, name = "Deleted Service", serviceType = ServiceTypeEnum.PAM, deletedAt = Instant.now())
        val services = listOf(deletedService)

        `when`(getServices.execute()).thenReturn(services)

        mockMvc.perform(get("/api/v2/admin/services").param("active", "true"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `getServices should return empty list when no services`() {
        `when`(getServices.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/admin/services"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }
}
