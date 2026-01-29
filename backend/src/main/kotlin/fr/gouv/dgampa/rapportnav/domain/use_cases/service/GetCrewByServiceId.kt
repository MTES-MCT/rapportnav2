package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
@UseCase
class GetCrewByServiceId(
    private val agentRepo: IAgent2Repository,
) {
    fun execute(serviceId: Int):  List<AgentEntity2> {
        val agents =  agentRepo
            .findByServiceId(serviceId = serviceId)
            .sortedBy { it.role?.priority }
            .map { AgentEntity2.fromAgentModel(it) }
        return agents
    }
}
