package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetPorts
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Port
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/ports")
class PortRestController(
    private val getPorts: GetPorts
) {

    @GetMapping
    @Operation(summary = "Get port list filtered by search query")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of ports", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Port::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    fun getPorts(@RequestParam search: String): List<Port> {
        return getPorts.execute()
            .filter { it.name.startsWith(search, ignoreCase = true) }
            .sortedBy { it.name }
            .map { Port.fromPortEntity(it) }
    }
}

