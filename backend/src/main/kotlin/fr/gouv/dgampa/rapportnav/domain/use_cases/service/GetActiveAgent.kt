package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository

@UseCase
class GetActiveAgent(
    private val agentRepo: IAgentRepository,
) {
    fun execute(): List<AgentEntity> {
        return agentRepo.findAllActive().map { AgentEntity.fromAgentModel(it) }
    }
}
