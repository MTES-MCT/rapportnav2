package fr.gouv.gmampa.rapportnav.infrastructure.api.public_api.inquiry.v1

import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryConclusionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryOriginType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiriesByVesselId
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.infrastructure.api.ControllersExceptionHandler
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1.InquiryPublicController
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.util.*

@WebMvcTest(InquiryPublicController::class)
@ContextConfiguration(classes = [InquiryPublicController::class, ControllersExceptionHandler::class, JacksonConfig::class])
@AutoConfigureMockMvc(addFilters = false)
class InquiryPublicControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var getInquiriesByVesselId: GetInquiriesByVesselId

    @MockitoBean
    private lateinit var getServices: GetServices

    @Test
    fun `should return inquiries of a vessel with service name`() {
        val id = UUID.randomUUID()
        val startDateTimeUtc = Instant.parse("2021-01-01T00:00:00Z")
        `when`(getInquiriesByVesselId.execute(vesselId = 42, startDateTimeUtc = startDateTimeUtc)).thenReturn(
            listOf(
                InquiryEntity(
                    id = id,
                    type = "FISH",
                    serviceId = 6,
                    agentId = 12,
                    origin = InquiryOriginType.entries.first(),
                    status = InquiryStatusType.CLOSED,
                    conclusion = InquiryConclusionType.entries.first(),
                    startDateTimeUtc = Instant.parse("2024-03-01T10:00:00Z"),
                    endDateTimeUtc = Instant.parse("2024-03-15T10:00:00Z"),
                    vessel = VesselEntity(vesselId = 42)
                )
            )
        )
        `when`(getServices.execute()).thenReturn(
            listOf(ServiceEntity(id = 6, name = "ULAM 56", serviceType = ServiceTypeEnum.entries.first()))
        )

        mockMvc.perform(
            get("/api/v1/public/inquiries")
                .param("vesselId", "42")
                .param("startDateTimeUtc", "2021-01-01T00:00:00Z")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(id.toString()))
            .andExpect(jsonPath("$[0].vesselId").value(42))
            .andExpect(jsonPath("$[0].type").value("FISH"))
            .andExpect(jsonPath("$[0].status").value("CLOSED"))
            .andExpect(jsonPath("$[0].service.id").value(6))
            .andExpect(jsonPath("$[0].service.name").value("ULAM 56"))
            .andExpect(jsonPath("$[0].agentId").doesNotExist())

        verify(getInquiriesByVesselId).execute(vesselId = 42, startDateTimeUtc = startDateTimeUtc)
    }

    @Test
    fun `should use the default start date when none is given and not load services when empty`() {
        `when`(getInquiriesByVesselId.execute(vesselId = 42, startDateTimeUtc = null)).thenReturn(emptyList())

        mockMvc.perform(get("/api/v1/public/inquiries").param("vesselId", "42"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))

        verify(getInquiriesByVesselId).execute(vesselId = 42, startDateTimeUtc = null)
        verifyNoInteractions(getServices)
    }

    @Test
    fun `should return 400 when vesselId is missing`() {
        mockMvc.perform(get("/api/v1/public/inquiries"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when startDateTimeUtc is not a valid instant`() {
        mockMvc.perform(
            get("/api/v1/public/inquiries")
                .param("vesselId", "42")
                .param("startDateTimeUtc", "not-a-date")
        )
            .andExpect(status().isBadRequest)
    }
}
