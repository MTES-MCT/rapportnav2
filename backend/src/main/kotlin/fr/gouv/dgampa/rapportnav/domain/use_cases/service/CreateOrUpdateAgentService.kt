package fr.gouv.dgampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentServiceModel
import kotlin.jvm.optionals.getOrNull

@UseCase
class CreateOrUpdateAgentService(
    private val agentRepo: IAgentRepository,
    private val roleRepo: IAgentRoleRepository,
    private val serviceRepo: IServiceRepository,
    private val agentServiceRepo: IAgentServiceRepository,
) {
    fun execute(input: AgentServiceInput): AgentServiceEntity? {
        if (input.agentId == null || input.serviceId == null) return null

        val agent =
            agentRepo.findById(input.agentId) ?: throw Exception("create or Update Agent on service: Agent not found")
        val service = serviceRepo.findById(input.serviceId).getOrNull()
            ?: throw Exception("create or Update Agent on service: Service not found")
        val role = if (input.roleId != null) roleRepo.findById(input.roleId) else null

        return agentServiceRepo.save(
            AgentServiceModel(
                role = role,
                agent = agent,
                id = input.id,
                service = service
            )
        ).let { AgentServiceEntity.fromAgentServiceModel(it) }
    }
}
