package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
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
@RequestMapping("/api/v2/resources")
class ControlUnitResourceRestController(
    private val getControlUnitResources: GetControlUnitResources
) {

    @GetMapping
    @Operation(summary = "Get the list of control unit resources")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of control unit resources", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = ControlUnitResourceEnv::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    fun getAll(): List<ControlUnitResourceEnv> {
        return getControlUnitResources.execute()
    }
}
