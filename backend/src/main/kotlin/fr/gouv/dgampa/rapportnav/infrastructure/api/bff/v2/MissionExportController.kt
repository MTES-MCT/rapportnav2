package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportModeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionReports
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controller responsible for exporting mission reports.
 *
 * This endpoint allows the generation of various types of documents for one or multiple missions, such as:
 * - Rapport de patrouille
 * - Tableaux AEM
 *
 * Depending on the provided parameters, the exported document(s) will be tailored to the selected report type
 * and export mode.
 */

data class ExportBodyRequest(
    @field:NotEmpty(message = "missionIds cannot be empty")
    val missionIds: List<Int>,
    @field:NotNull(message = "exportMode cannot be null")
    val exportMode: ExportModeEnum,
    @field:NotNull(message = "reportType cannot be null")
    val reportType: ExportReportTypeEnum
)

data class ErrorResponse(
    val status: Int,
    val message: String,
    val details: String? = null
)

@RestController
@RequestMapping("/api/v2/missions")
class MissionExportController(
    private val exportMissionReports: ExportMissionReports,
) {

    private val logger = LoggerFactory.getLogger(MissionExportController::class.java)

    @PostMapping("/export")
    @Operation(
        summary = "Compute amd export reports on a single or multiple missions",
        description = "Computes and exports Rapport de Patrouille or AEM files",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Exports successful",
                content = [Content(schema = Schema(implementation = MissionExportEntity::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid mission IDs or input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Unexpected internal error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun exportMissionReports(
        @Valid @RequestBody request: ExportBodyRequest
    ): MissionExportEntity {
        return exportMissionReports.execute(
            missionIds = request.missionIds.distinct(),
            exportMode = request.exportMode,
            reportType = request.reportType
        )
    }

}
