package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillReport
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.BackfillPamResourceUsage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

// Under /api/v2/admin/**, so restricted to ROLE_ADMIN by SecurityConfig.
@RestController
@RequestMapping("/api/v2/admin/resource-usage")
class ResourceUsageAdminController(
    private val backfillPamResourceUsage: BackfillPamResourceUsage,
) {
    @PostMapping("backfill-pam")
    @Operation(
        summary = "Copy the PAM metrics of mission_general_info onto the PAM ship rows of mission_resource_usage",
        description = "Idempotent. Dry run by default: pass dryRun=false to write."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "backfill report", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = PamBackfillReport::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "500", description = "MonitorEnv resources could not be loaded", content = [Content()])
        ]
    )
    fun backfillPam(@RequestParam(defaultValue = "true") dryRun: Boolean): PamBackfillReport {
        return backfillPamResourceUsage.execute(dryRun = dryRun)
    }
}
