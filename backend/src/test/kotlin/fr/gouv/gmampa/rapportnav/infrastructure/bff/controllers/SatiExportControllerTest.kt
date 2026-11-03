package fr.gouv.gmampa.rapportnav.infrastructure.bff.controllers

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.domain.entities.export.ActionDocumentTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportAuditEntity
import fr.gouv.dgampa.rapportnav.domain.entities.export.ExportedDocumentEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportSatiPdf
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2.SatiExportController
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ApiKeyAuthenticationFilter
import org.junit.jupiter.api.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [RapportNavApplication::class])
@WebMvcTest(SatiExportController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class SatiExportControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var exportSatiPdf: ExportSatiPdf

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    private val pdf = "%PDF-1.7 fake".toByteArray()
    private val document = ExportedDocumentEntity(
        content = pdf,
        audit = ExportAuditEntity(ownerId = "761", actionId = "12345", documentType = ActionDocumentTypeEnum.SATI),
    )
    private val url = "/api/v2/owners/761/actions/12345/sati/pdf"

    @Test
    fun `should return the pdf as an attachment and audit the download`() {
        `when`(exportSatiPdf.execute(ownerId = "761", actionId = "12345")).thenReturn(document)

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_PDF))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_PDF))
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"sati-12345.pdf\""))
            .andExpect(content().bytes(pdf))

        verify(exportSatiPdf).markAsDownloaded(document)
    }

    @Test
    fun `should return the pdf even when the client accepts json only`() {
        `when`(exportSatiPdf.execute(ownerId = "761", actionId = "12345")).thenReturn(document)

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_PDF))
            .andExpect(content().bytes(pdf))
    }

    @Test
    fun `should return 400 (never 403) when the action is not found`() {
        `when`(exportSatiPdf.execute(ownerId = "761", actionId = "12345")).thenThrow(
            BackendUsageException(code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, message = "not found")
        )

        mockMvc.perform(get(url).accept(MediaType.APPLICATION_PDF, MediaType.APPLICATION_PROBLEM_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.type").value("urn:rapportnav:error:usage:COULD_NOT_FIND_EXCEPTION"))

        verify(exportSatiPdf, never()).markAsDownloaded(any())
    }
}
