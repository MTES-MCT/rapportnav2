package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.ControlResource
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgent
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DisableAgent
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.UpdateResource
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetServiceForUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Agent
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.ResourceInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/manage/services/{serviceId}")
class ServiceManageController(
    private val disableAgent: DisableAgent,
    private val updateResource: UpdateResource,
    private val getServiceForUser: GetServiceForUser,
    private val getCrewByServiceId: GetCrewByServiceId,
    private val createOrUpdateAgent: CreateOrUpdateAgent
) {
    private val logger = LoggerFactory.getLogger(ServiceManageController::class.java)

    @GetMapping("agents")
    @Operation(summary = "Get the list of all agents on a service")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Found list of agents", content = [(Content(
                mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = Agent::class)))
            ))]
        ), ApiResponse(responseCode = "404", description = "Did not find any agents list", content = [Content()])]
    )
    fun agents(
        @PathVariable serviceId: Int
    ): List<Agent?> {
        checkServiceOwnerShip(serviceId = serviceId)
        return getCrewByServiceId.execute(serviceId = serviceId)
            .map { Agent.fromAgentEntity(it) }
    }

    @PostMapping("agents")
    @Operation(summary = "Create Agent")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Create Agent", content = [(Content(
                mediaType = "application/json", schema = Schema(implementation = Agent::class)
            ))]
        ), ApiResponse(responseCode = "404", description = "Could not create no Agent", content = [Content()])]
    )

    fun addAgent(@RequestBody body: AgentInput2, @PathVariable serviceId: Int): Agent? {
        checkServiceOwnerShip(serviceId = serviceId)
        return createOrUpdateAgent.execute(
            getAgentInput(
                body = body, serviceId = serviceId
            )
        ).let { Agent.fromAgentEntity(it) }
    }

    @PutMapping("agents")
    @Operation(summary = "Update Agent")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "Create Agent", content = [(Content(
                mediaType = "application/json", schema = Schema(implementation = Agent::class)
            ))]
        ), ApiResponse(responseCode = "404", description = "Could not create no Agent", content = [Content()])]
    )

    fun updateAgent(
        @RequestBody body: AgentInput2, @PathVariable serviceId: Int
    ): Agent? {
        checkServiceOwnerShip(serviceId = serviceId)
        return createOrUpdateAgent.execute(
            input = getAgentInput(
                body = body, serviceId = serviceId
            )
        ).let { Agent.fromAgentEntity(it) }
    }

    @DeleteMapping("agents/{agentId}")
    @Operation(summary = "Disable Agent")
    @ApiResponse(responseCode = "404", description = "Could not disable Agent", content = [Content()])
    fun disableAgent(
        @PathVariable agentId: Int, @PathVariable serviceId: Int
    ) {
        checkServiceOwnerShip(serviceId = serviceId)
        disableAgent.execute(agentId)
    }

    @PutMapping("resources")
    @Operation(summary = "Update control units")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "update resources", content = [(Content(
                mediaType = "application/json", schema = Schema(implementation = ControlResource::class)
            ))]
        ), ApiResponse(responseCode = "404", description = "Could not update resources", content = [Content()])]
    )

    fun updateResource(
        @PathVariable serviceId: Int, @RequestBody body: ResourceInput
    ): ControlUnitResourceEnv? {
        checkServiceOwnerShip(serviceId = serviceId)
        checkControlUnitOwnerShip(controlUnit = body.controlUnitId)
        return updateResource.execute(input = body)
    }


    private fun checkServiceOwnerShip(serviceId: Int) {
        val service = getServiceForUser.execute()
        if (service?.id != serviceId) throw BackendUsageException(
            code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
            message = "Action not allowed for the user on the service: $serviceId"
        )
    }

    private fun checkControlUnitOwnerShip(controlUnit: Int) {
        val service = getServiceForUser.execute()
        if (service?.controlUnits?.contains(controlUnit) != true) throw BackendUsageException(
            code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
            message = "Action not allowed for the user on the control unit: $controlUnit"
        )
    }

    private fun getAgentInput(
        body: AgentInput2, serviceId: Int
    ): AgentInput2 = AgentInput2(
        id = body.id,
        cardId = body.cardId,
        userId = body.userId,
        roleId = body.roleId,
        serviceId = serviceId,
        lastName = body.lastName,
        firstName = body.firstName
    )
}
