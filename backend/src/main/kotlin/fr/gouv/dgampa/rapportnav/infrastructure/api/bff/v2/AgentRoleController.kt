package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentRoles
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentRole
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/agent_roles")
class AgentRoleController(
    private val getAgentRoles: GetAgentRoles
) {

    @GetMapping("")
    fun agentRoles(): List<AgentRole> {
        return getAgentRoles.execute().map { AgentRole.fromAgentRoleEntity(it) }
    }
}
