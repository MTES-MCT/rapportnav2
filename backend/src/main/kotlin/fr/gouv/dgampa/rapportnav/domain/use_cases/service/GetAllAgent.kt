package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository

@UseCase
class GetAllAgent(
    private val agentRepo: IAgent2Repository,
) {
    fun execute(): List<AgentEntity2> {
        return agentRepo.findAll().map { AgentEntity2.fromAgentModel(it) }
    }
}
