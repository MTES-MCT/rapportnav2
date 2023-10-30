package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.Agent
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetAgents(private val agentRepository: IAgentRepository) {

    private val logger = LoggerFactory.getLogger(GetAgents::class.java)


    fun execute(): List<AgentModel> {

        val agent1 = Agent(
            id = UUID.randomUUID(),
            firstName = "Aleck",
            lastName = "Vincent",
            deletedAt = null
        )
        return agentRepository.findAll()
       // return listOf(agent1)

    }
}