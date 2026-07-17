package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetAllMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SoftDeleteMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs.AdminPaginatedMissionOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v2/admin/missions")
class MissionAdminController(
    private val getAllMissions: GetAllMissions,
    private val softDeleteMission: SoftDeleteMission,
) {
    @GetMapping("")
    @Operation(summary = "Get all missions with pagination, most recent first")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "get missions", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AdminPaginatedMissionOutput::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not get missions", content = [Content()])
        ]
    )
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) searchId: String?
    ): AdminPaginatedMissionOutput {
        val result = getAllMissions.execute(page, size, searchId)
        return AdminPaginatedMissionOutput.fromPage(result)
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Soft-delete a mission by id")
    @ApiResponse(responseCode = "404", description = "Could not delete mission", content = [Content()])
    fun delete(@PathVariable id: UUID) {
        return softDeleteMission.execute(id = id)
    }
}
