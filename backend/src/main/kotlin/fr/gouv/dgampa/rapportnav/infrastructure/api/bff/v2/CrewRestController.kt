package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.crew.ServiceWithAgents
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
@RequestMapping("/api/v2/crews")
class CrewRestController(
    private val getCrewByServiceId: GetCrewByServiceId,
    private val getServices: GetServices,
) {
    private val logger = LoggerFactory.getLogger(CrewRestController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of crew and roles per service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of crew agent by service", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = ServiceWithAgents::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any crew agent", content = [Content()])
        ]
    )
    fun getAllCrews(): List<ServiceWithAgents> {
        val services = getServices.execute()
        return try {
            services.map { service ->
                ServiceWithAgents(
                    service = service,
                    agents = service.id?.let { getCrewByServiceId.execute(serviceId = it) } ?: emptyList()
                )}
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getCrossControlByServiceId:", e)
            return emptyList()
        }
    }
}
