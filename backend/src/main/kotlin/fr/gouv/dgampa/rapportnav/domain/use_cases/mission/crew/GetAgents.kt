package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository

@UseCase
class GetAgents(private val agentRepository: IAgentRepository) {
    fun execute(): List<AgentEntity> {
        return agentRepository.findAll().map { AgentEntity.fromAgentModel(it) }
    }
}
