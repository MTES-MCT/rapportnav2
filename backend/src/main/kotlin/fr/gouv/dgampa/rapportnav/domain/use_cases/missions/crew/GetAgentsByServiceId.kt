package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository

@UseCase
class GetAgentsByServiceId(private val agentRepository: IAgentRepository) {

  fun execute(serviceId: Int): List<AgentEntity> {
    val a = agentRepository.findByServiceId(serviceId = serviceId)
    return agentRepository.findByServiceId(serviceId = serviceId).map { it.toAgentEntity() }
  }

}
