package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Agent2
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
@RequestMapping("/api/v2/agents")
class AgentRestController(
    private val getUserFromToken: GetUserFromToken,
    private val getCrewByServiceId: GetCrewByServiceId,
) {

    @GetMapping
    @Operation(summary = "Get the list of all agents for the current user's service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of agents", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Agent2::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any agents list", content = [Content()])
        ]
    )
    fun agents(): List<Agent2> {
        val user = getUserFromToken.execute() ?: return emptyList()
        val serviceId = user.serviceId ?: return emptyList()
        return getCrewByServiceId.execute(serviceId).mapNotNull { Agent2.fromAgentEntity(it) }
    }
}
