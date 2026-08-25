package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.infraction.v1

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetSatiInfraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.infraction.v1.adapters.output.InfractionListOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.infraction.v1.adapters.output.InfractionOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.infraction.v1.adapters.output.PaginatedOutput
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/public/infraction")
@Tag(name = "Infraction", description = "Public API for Infraction records")
class InfractionPublicController(
    private val getSatiInfraction: GetSatiInfraction,
    private val getInfractionById: GetInfractionById
) {

    @GetMapping
    @Operation(
        summary = "List Infraction records",
        description = "Paginated, sortable and searchable list of Infraction records.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Infraction records successfully retrieved",
                content = [Content(schema = Schema(implementation = PaginatedOutput::class))]
            )
        ]
    )
    fun getInfractionList(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "updatedAt,desc") sort: String,
        @RequestParam(required = false) search: String?
    ): PaginatedOutput<InfractionListOutput> {
        getSatiInfraction.execute(page = page, size = size, sort = sort, search = search)
        TODO("Not yet implemented")
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a single Infraction record",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Infraction record found",
                content = [Content(schema = Schema(implementation = InfractionOutput::class))]
            ),
            ApiResponse(responseCode = "404", description = "No Infraction record with this id")
        ]
    )
    fun getInfractionById(@PathVariable id: UUID): ResponseEntity<InfractionOutput> {
        getInfractionById.execute(id)
        TODO("Not yet implemented")
    }
}
