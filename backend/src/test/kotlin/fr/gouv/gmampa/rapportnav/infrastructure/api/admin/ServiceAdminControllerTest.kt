package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.ServiceAdminController
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
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

@WebMvcTest(ServiceAdminController::class)
@ContextConfiguration(classes = [ServiceAdminController::class, ControllersExceptionHandler::class])
@AutoConfigureMockMvc(addFilters = false)
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

    private val jsonMapper = JsonMapper.builder().build()
    private val serviceEntity = ServiceEntity(id = 1, name = "PAM Jeanne Barret", serviceType = ServiceTypeEnum.PAM)

    @Test
    fun `getServices should return list of services`() {
        `when`(getServices.execute()).thenReturn(listOf(serviceEntity))

        mockMvc.perform(get("/api/v2/admin/services"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value("PAM Jeanne Barret"))
    }

    @Test
    fun `getServices should return 500 on exception`() {
        `when`(getServices.execute()).thenThrow(RuntimeException("DB error"))

        mockMvc.perform(get("/api/v2/admin/services"))
            .andExpect(status().isInternalServerError)
    }

    @Test
    fun `getServiceById should return service when found`() {
        `when`(getServiceById.execute(1)).thenReturn(serviceEntity)

        mockMvc.perform(get("/api/v2/admin/services/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("PAM Jeanne Barret"))
    }

    @Test
    fun `getServiceById should return null on exception`() {
        `when`(getServiceById.execute(1)).thenThrow(RuntimeException("error"))

        mockMvc.perform(get("/api/v2/admin/services/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `create should return created service`() {
        `when`(createOrUpdateService.execute(any())).thenReturn(serviceEntity)

        val body = mapOf("id" to null, "name" to "PAM Jeanne Barret", "serviceType" to "PAM")

        mockMvc.perform(
            post("/api/v2/admin/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("PAM Jeanne Barret"))
    }

    @Test
    fun `create should return null on exception`() {
        `when`(createOrUpdateService.execute(any())).thenThrow(RuntimeException("error"))

        val body = mapOf("id" to null, "name" to "PAM Jeanne Barret", "serviceType" to "PAM")

        mockMvc.perform(
            post("/api/v2/admin/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `update should return updated service`() {
        `when`(createOrUpdateService.execute(any())).thenReturn(serviceEntity)

        val body = mapOf("id" to 1, "name" to "PAM Jeanne Barret", "serviceType" to "PAM")

        mockMvc.perform(
            put("/api/v2/admin/services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("PAM Jeanne Barret"))
    }

    @Test
    fun `delete should succeed`() {
        mockMvc.perform(delete("/api/v2/admin/services/1"))
            .andExpect(status().isOk)

        verify(deleteService).execute(id = 1)
    }
}