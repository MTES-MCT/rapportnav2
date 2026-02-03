package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import kotlin.jvm.optionals.getOrNull

@UseCase
class MigrateAgent(
    private val agentRepo: IAgent2Repository,
    private val serviceRepo: IServiceRepository,
    private val createOrUpdateAgent: CreateOrUpdateAgent
) {
    fun execute(input: AgentInput2): AgentEntity2 {
        val agentId = input.id ?: throw BackendUsageException(
            code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
            message = "MigrateAgent: agent id is required"
        )

        val agent = agentRepo.findById(id = agentId)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "MigrateAgent: agent not found for id=$agentId"
            )

        val newService = serviceRepo.findById(id = input.serviceId).getOrNull()
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "MigrateAgent: service not found for serviceId=${input.serviceId}"
            )

        agentRepo.disabledById(id = agent.id!!)

        return createOrUpdateAgent.execute(
            AgentInput2(
                userId = input.userId,
                roleId = input.roleId,
                lastName = input.lastName,
                firstName = input.firstName,
                serviceId = newService.id!!
            )
        )
    }
}
