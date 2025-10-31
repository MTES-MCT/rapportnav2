package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgentService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteAgentService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetAgentService
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentService
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Service
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.ServiceWithAgent
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/agent_services")
class AgentServiceAdminRestController(
    private val getAgentServices: GetAgentService,
    private val deleteAgentService: DeleteAgentService,
    private val createOrUpdateService: CreateOrUpdateAgentService,
) {
    private val logger = LoggerFactory.getLogger(AgentServiceAdminRestController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of crew and roles per service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of crew agent by service", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = ServiceWithAgent::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any crew agent", content = [Content()])
        ]
    )
    fun getAllCrews(): List<ServiceWithAgent> {
        val agentServices = getAgentServices.execute().groupBy { it.service }
        return try {
            agentServices.map { agentService ->
                ServiceWithAgent(
                    service = agentService.key.let { Service.fromServiceEntity(it) },
                    agentServices = agentService.value.map { AgentService.fromAgentServiceEntity(it) }
                        .sortedBy { it.role?.priority }
                )
            }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getCrossControlByServiceId:", e)
            return emptyList()
        }
    }

    @PostMapping("")
    @Operation(summary = "Add member on a service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Add member on a service", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Service::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not cAdd member on a service", content = [Content()])
        ]
    )
    fun create(@RequestBody body: AgentServiceInput): AgentService? {
        return try {
            createOrUpdateService.execute(input = body)?.let { AgentService.fromAgentServiceEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv service : ", e)
            return null
        }
    }

    @PutMapping("")
    @Operation(summary = "Update member on a service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Update member on a service", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Service::class)
                    ))
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Could not update member on a service",
                content = [Content()]
            )
        ]
    )
    fun update(
        @RequestBody body: AgentServiceInput
    ): AgentService? {
        return try {
            createOrUpdateService.execute(input = body)?.let { AgentService.fromAgentServiceEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating service : ", e)
            return null
        }
    }

    @DeleteMapping("{agentServiceId}")
    @Operation(summary = "Delete a member on a service")
    @ApiResponse(responseCode = "404", description = "Could not delete service", content = [Content()])
    fun delete(
        @PathVariable(name = "agentServiceId") agentServiceId: Int
    ) {
        return try {
            deleteAgentService.execute(id = agentServiceId)
        } catch (e: Exception) {
            logger.error("Error while deleting a member on a service : ", e)
        }
    }
}
