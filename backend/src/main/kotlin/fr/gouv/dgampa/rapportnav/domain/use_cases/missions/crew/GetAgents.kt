package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import org.slf4j.LoggerFactory

@UseCase
class GetAgents(private val agentRepository: IAgentRepository) {

    private val logger = LoggerFactory.getLogger(GetAgents::class.java)


    fun execute(): List<AgentEntity> {
        return agentRepository.findAll().map { it.toAgentEntity() }
    }
}
