package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetCrewByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.crew.ServiceWithAgents
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/missions/{missionId}/crews")
class CrewRestController(
    private val getCrewByServiceId: GetCrewByServiceId,
    private val getServices: GetServices,
) {
    private val logger = LoggerFactory.getLogger(CrewRestController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of crew and roles per service")
    fun getAllCrews(): List<ServiceWithAgents> {
        val services = getServices.execute()
        return services.map { service ->
            ServiceWithAgents(
                service = service,
                agents = service.id?.let { getCrewByServiceId.execute(serviceId = it) } ?: emptyList()
            )
        }

    }
}
