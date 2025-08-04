package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentRoles
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentRole
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
@RequestMapping("/api/v2/agent_roles")
class AgentRoleController(
    private val getAgentRoles: GetAgentRoles
) {

    @GetMapping("")
    @Operation(summary = "Get the list of agent roles")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of agent's role", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = AgentRole::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any agent's roles list", content = [Content()])
        ]
    )
    fun agentRoles(): List<AgentRole> {
        return getAgentRoles.execute().map { AgentRole.fromAgentRoleEntity(it) }
    }
}
