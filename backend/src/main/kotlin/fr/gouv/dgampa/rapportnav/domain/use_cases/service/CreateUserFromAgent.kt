package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.CreateUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput

@UseCase
class CreateUserFromAgent(
    private val createUser: CreateUser,
    private val agentRepo: IAgent2Repository,
) {
    fun execute(agentId: Int, input: AuthRegisterDataInput): AgentEntity2? {
        val agent = agentRepo.findById(id = agentId)
        if(agent == null || agent.userId != null) throw Exception("Unknown agent or user already exists")

        val user = createUser.execute(input)
        agent.userId = user?.id
        return agentRepo.save(agent).let { AgentEntity2.fromAgentModel(it) }
    }
}
