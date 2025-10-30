package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository

@UseCase
class GetAgentsByServiceId(private val agentServiceRepository: IAgentServiceRepository) {

    fun execute(serviceId: Int): List<AgentServiceEntity> {
        return agentServiceRepository.findByServiceId(serviceId = serviceId)
            .map { AgentServiceEntity.fromAgentServiceModel(it) }
    }
}
