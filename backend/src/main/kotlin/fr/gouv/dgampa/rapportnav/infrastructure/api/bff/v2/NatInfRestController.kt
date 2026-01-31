package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetNatinfs
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Natinf
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/natinfs")
class NatInfRestController(
    private val getNatinfs: GetNatinfs
) {

    @GetMapping
    @Operation(summary = "Get the list of natinfs")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of natinfs", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Natinf::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    fun getNatinfs(): List<Natinf> {
        return getNatinfs.execute().map { Natinf.fromNatinfEntity(it) }
    }
}

