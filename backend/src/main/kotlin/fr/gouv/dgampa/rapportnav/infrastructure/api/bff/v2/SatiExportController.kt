package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportSatiPdf
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller responsible for exporting the SATI document of a fish control action as PDF.
 * Each download is audited (see export_audit).
 */
@RestController
@RequestMapping("/api/v2/owners/{ownerId}/actions/{actionId}/sati")
class SatiExportController(
    private val exportSatiPdf: ExportSatiPdf,
) {

    // no `produces`: the content type is set on the response, so the default `Accept: application/json`
    // of the frontend client does not end up in a 406
    @GetMapping("/pdf")
    @Operation(
        summary = "Download the SATI pdf of a fish control action",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "PDF file",
                content = [Content(mediaType = MediaType.APPLICATION_PDF_VALUE, schema = Schema(type = "string", format = "binary"))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Action not found, not a fish control or not complete for stats",
                content = [Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)]
            ),
        ]
    )
    fun exportPdf(
        @PathVariable(name = "ownerId") ownerId: String,
        @PathVariable(name = "actionId") actionId: String,
        response: HttpServletResponse,
    ) {
        val document = exportSatiPdf.execute(ownerId = ownerId, actionId = actionId)

        response.contentType = MediaType.APPLICATION_PDF_VALUE
        response.setContentLength(document.content.size)
        response.setHeader(
            HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment().filename("sati-$actionId.pdf").build().toString()
        )
        response.outputStream.write(document.content)
        // flushing throws if the client is gone: an aborted download is then not audited
        response.flushBuffer()

        exportSatiPdf.markAsDownloaded(document)
    }
}
