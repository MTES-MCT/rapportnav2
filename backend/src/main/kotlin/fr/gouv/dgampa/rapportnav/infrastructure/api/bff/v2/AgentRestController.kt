package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgents
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Agent
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/agents")
class AgentRestController(
    private val getUserFromToken: GetUserFromToken,
    private val getAgents: GetAgents,
) {
    private val logger = LoggerFactory.getLogger(AgentRestController::class.java)

    @GetMapping
    @Operation(summary = "Get the list of all agents")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of agents", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Agent::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any agents list", content = [Content()])
        ]
    )
    fun agents(): List<Agent>? {
        return try {
            getAgents.execute().map { Agent.fromAgentEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint agents:", e)
            listOf()
        }
    }
}
