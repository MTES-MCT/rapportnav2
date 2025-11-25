package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import kotlin.jvm.optionals.getOrNull

@UseCase
class CreateOrUpdateAgent2(
    private val agentRepo: IAgent2Repository,
    private val roleRepo: IAgentRoleRepository,
    private val serviceRepo: IServiceRepository
) {
    fun execute(input: AgentInput2): AgentEntity2? {
        var agent: AgentModel2? = null
        agent = if (input.id == null)  create(input) else update(input)
        agent.role = if (input.roleId != null) roleRepo.findById(input.roleId) else null
        return agent.let { model -> agentRepo.save(model).let { AgentEntity2.fromAgentModel(it) } }
    }

    fun create(input: AgentInput2): AgentModel2 {
        val service = serviceRepo.findById(input.serviceId).getOrNull()
            ?: throw Exception("Create Agent: Service not found")
        return AgentModel2(
            id = input.id,
            service = service,
            userId = input.userId,
            lastName = input.lastName,
            firstName = input.firstName,
        )
    }

    fun update(input: AgentInput2): AgentModel2 {
        if (input.id == null) throw Exception("Update Agent: id is null")
        val agent = agentRepo.findById(id = input.id!!) ?: throw Exception("Update Agent: not found")

        return AgentModel2(
            id = input.id,
            userId = input.userId,
            service = agent.service,
            lastName = input.lastName,
            firstName = input.firstName,
        )
    }
}
