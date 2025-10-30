package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository

@UseCase
class GetActiveCrewForService(
    private val agentServiceRepo: IAgentServiceRepository,
) {
    fun execute(serviceId: Int): List<AgentServiceEntity> {
        val newMissionCrews = agentServiceRepo.findByServiceId(serviceId)
            .filter { it.disabledAt == null } // only keep active crew, i.e. not disabled
            .mapNotNull { AgentServiceEntity.fromAgentServiceModel(it)}
        return newMissionCrews
    }
}
