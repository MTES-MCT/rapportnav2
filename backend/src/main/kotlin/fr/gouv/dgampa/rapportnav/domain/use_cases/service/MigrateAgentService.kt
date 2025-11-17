package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgent2Repository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import kotlin.jvm.optionals.getOrNull

@UseCase
class MigrateAgentService(
    private val agentRepo: IAgent2Repository,
    private val serviceRepo: IServiceRepository,
    private val createOrUpdateAgent2: CreateOrUpdateAgent2
) {
    fun execute(input: AgentInput2): AgentEntity2? {
        if (input.id == null) throw Exception("Update Service: at least on id is null")

        val agent = agentRepo.findById(id = input.id!!)
        val newService = serviceRepo.findById(id = input.serviceId).getOrNull()
        if (agent == null || newService == null) throw Exception("Update Service: Service or agent not found")

        agentRepo.disabledById(id = agent.id!!)

        return createOrUpdateAgent2.execute(
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
