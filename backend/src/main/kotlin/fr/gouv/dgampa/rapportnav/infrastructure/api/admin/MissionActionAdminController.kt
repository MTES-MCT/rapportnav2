package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllMissionActions
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs.AdminPaginatedMissionActionOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/mission-actions")
class MissionActionAdminController(
    private val getAllMissionActions: GetAllMissionActions,
) {
    @GetMapping("")
    @Operation(summary = "Get all mission actions with pagination, most recent first")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "get mission actions", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AdminPaginatedMissionActionOutput::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not get mission actions", content = [Content()])
        ]
    )
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) searchId: String?,
        @RequestParam(required = false) searchOwnerId: String?
    ): AdminPaginatedMissionActionOutput {
        val result = getAllMissionActions.execute(page, size, searchId, searchOwnerId)
        return AdminPaginatedMissionActionOutput.fromPage(result)
    }
}
