package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Agent
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/agents")
class AgentAdminController(
    private val getAgent: GetAllAgent,

    private val deleteAgent: DeleteAgent,
    private val disableAgent: DisableAgent,
    private val migrateAgent: MigrateAgent,
    private val createOrUpdateAgent: CreateOrUpdateAgent
) {
    private val logger = LoggerFactory.getLogger(AgentAdminController::class.java)

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
    fun agents(): List<Agent?> {
        return try {
            getAgent.execute()
                .map { Agent.fromAgentEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint agents:", e)
            listOf()
        }
    }

    @PostMapping("")
    @Operation(summary = "Create Agent")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create Agent", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Agent::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no Agent", content = [Content()])
        ]
    )
    fun create(@RequestBody body: AgentInput2): Agent? {
        return try {
            createOrUpdateAgent.execute(body).let { Agent.fromAgentEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating Agent : ", e)
            return null
        }
    }

    @PutMapping("")
    @Operation(summary = "Update Agent")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create Agent", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Agent::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no Agent", content = [Content()])
        ]
    )
    fun update(
        @RequestBody body: AgentInput2
    ): Agent? {
        return try {
            createOrUpdateAgent.execute(input = body).let { Agent.fromAgentEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while updating Agent : ", e)
            return null
        }
    }

    @PostMapping("{agentId}/migrate")
    @Operation(summary = "Migrate Agent from service A to service B")
    @ApiResponse(responseCode = "404", description = "Could not migrate Agent", content = [Content()])
    fun migrate(
        @PathVariable(name = "agentId") agentId: Int,
        @RequestBody body: AgentInput2
    ): AgentEntity? {
        if (agentId != body.id) throw IllegalArgumentException("Wrong agent Id: ${body.id}")
        return try {
            migrateAgent.execute(body)
        } catch (e: Exception) {
            logger.error("Error while migrating Agent : ", e)
            return null
        }
    }

    @PostMapping("{agentId}/disable")
    @Operation(summary = "Disable Agent")
    @ApiResponse(responseCode = "404", description = "Could not disable Agent", content = [Content()])
    fun disable(
        @PathVariable(name = "agentId") agentId: Int
    ) {
        return try {
            disableAgent.execute(agentId)
        } catch (e: Exception) {
            logger.error("Error while disable Agent : ", e)
        }
    }


    @DeleteMapping("{agentId}")
    @Operation(summary = "Delete a Agent create by the unity")
    @ApiResponse(responseCode = "404", description = "Could not delete Agent", content = [Content()])
    fun delete(
        @PathVariable(name = "agentId") agentId: Int
    ) {
        return try {
            deleteAgent.execute(id = agentId)
        } catch (e: Exception) {
            logger.error("Error while deleting Agent : ", e)
        }
    }
}
