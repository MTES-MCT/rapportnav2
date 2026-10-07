package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.sati.GetSatiById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.sati.GetSatiList
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.sati.PatchSatiStatus
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.input.SatiStatusPatchInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.output.SatiOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.output.SatiOutputMapper
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.output.SatiPaginatedOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/public/sati")
@Tag(name = "Sati", description = "Public API for Sati inspection records")
class SatiPublicController(
    private val getSatiList: GetSatiList,
    private val getSatiById: GetSatiById,
    private val patchSatiStatus: PatchSatiStatus
) {

    @GetMapping
    @Operation(
        summary = "List Sati records",
        description = "Paginated, sortable, filterable and searchable list of Sati records.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Sati records successfully retrieved",
                content = [Content(schema = Schema(implementation = SatiPaginatedOutput::class))]
            )
        ]
    )
    fun getSatiList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "updatedAt,desc") sort: String,
        @RequestParam(required = false) module: SatiModuleType?,
        @RequestParam(required = false) status: SatiStatusType?,
        @RequestParam(required = false) actionId: String?,
        @RequestParam(required = false) search: String?
    ): SatiPaginatedOutput {
        val result = getSatiList.execute(page, size, sort, module, status, actionId, search)
        return SatiPaginatedOutput.fromPage(result)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a single Sati record",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Sati record found",
                content = [Content(schema = Schema(implementation = SatiOutput::class))]
            ),
            ApiResponse(responseCode = "404", description = "No Sati record with this id")
        ]
    )
    fun getSatiById(@PathVariable id: UUID): ResponseEntity<SatiOutput> {
        val entity = getSatiById.execute(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SatiOutputMapper.toOutput(entity))
    }

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Update the status of a Sati record",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Status successfully updated",
                content = [Content(schema = Schema(implementation = SatiOutput::class))]
            ),
            ApiResponse(responseCode = "404", description = "No Sati record with this id")
        ]
    )
    fun patchSatiStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: SatiStatusPatchInput
    ): ResponseEntity<SatiOutput> {
        val status = requireNotNull(request.status)
        val entity = patchSatiStatus.execute(id, status) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SatiOutputMapper.toOutput(entity))
    }
}
