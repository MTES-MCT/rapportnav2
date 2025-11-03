package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgentRole
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteAgentRole
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentRole
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/agent_roles")
class AgentRoleAdminController(
    private val deleteAgentRole: DeleteAgentRole,
    private val createOrUpdateAgentRole: CreateOrUpdateAgentRole
) {
    private val logger = LoggerFactory.getLogger(AgentRoleAdminController::class.java)

    @PostMapping("")
    @Operation(summary = "Create AgentRole")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create AgentRole", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AgentRole::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no AgentRole", content = [Content()])
        ]
    )
    fun create(@RequestBody body: AgentRole): AgentRole? {
        return try {
            createOrUpdateAgentRole.execute(body.toAgentRoleEntity()).let { AgentRole.fromAgentRoleEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating AgentRole : ", e)
            return null
        }
    }

    @PutMapping("")
    @Operation(summary = "Update AgentRole")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create AgentRole", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AgentRole::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no AgentRole", content = [Content()])
        ]
    )
    fun update(
        @RequestBody body: AgentRole
    ): AgentRole? {
        return try {
            createOrUpdateAgentRole.execute(entity = body.toAgentRoleEntity()).let { AgentRole.fromAgentRoleEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while updating AgentRole : ", e)
            return null
        }
    }


    @DeleteMapping("{AgentRoleId}")
    @Operation(summary = "Delete a AgentRole create by the unity")
    @ApiResponse(responseCode = "404", description = "Could not delete AgentRole", content = [Content()])
    fun delete(
        @PathVariable(name = "AgentRoleId") AgentRoleId: Int
    ) {
        return try {
            deleteAgentRole.execute(id = AgentRoleId)
        } catch (e: Exception) {
            logger.error("Error while deleting AgentRole : ", e)
        }
    }
}
