package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import kotlin.jvm.optionals.getOrNull

@UseCase
class CreateOrUpdateAgent(
    private val agentRepo: IAgent2Repository,
    private val roleRepo: IAgentRoleRepository,
    private val serviceRepo: IServiceRepository
) {
    fun execute(input: AgentInput2): AgentEntity2 {
        val agent = if (input.id == null) create(input) else update(input)
        agent.role = if (input.roleId != null) roleRepo.findById(input.roleId) else null
        return agentRepo.save(agent).let { AgentEntity2.fromAgentModel(it) }
    }

    private fun create(input: AgentInput2): AgentModel2 {
        val service = serviceRepo.findById(input.serviceId).getOrNull()
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "CreateOrUpdateAgent2: service not found for serviceId=${input.serviceId}"
            )
        return AgentModel2(
            id = input.id,
            service = service,
            userId = input.userId,
            lastName = input.lastName,
            firstName = input.firstName,
        )
    }

    private fun update(input: AgentInput2): AgentModel2 {
        val agentId = input.id ?: throw BackendUsageException(
            code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
            message = "CreateOrUpdateAgent2: id is required for update"
        )
        val agent = agentRepo.findById(id = agentId)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "CreateOrUpdateAgent2: agent not found for id=$agentId"
            )

        return AgentModel2(
            id = agentId,
            userId = input.userId,
            service = agent.service,
            lastName = input.lastName,
            firstName = input.firstName,
        )
    }
}
