package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Agent
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/agents")
class AgentRestController(
    private val getUserFromToken: GetUserFromToken,
    private val getAgentsByServiceId: GetAgentsByServiceId,
) {

    private val logger = LoggerFactory.getLogger(AgentRestController::class.java)

    @GetMapping
    fun agents(): List<Agent>? {
        return try {
            val user = getUserFromToken.execute()

            user?.serviceId?.let { serviceId ->
                val agents = getAgentsByServiceId.execute(serviceId)
                    .map { Agent.fromAgentEntity(it) }
                agents
            }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint agentsByServiceId:", e)
            null
        }
    }

}
