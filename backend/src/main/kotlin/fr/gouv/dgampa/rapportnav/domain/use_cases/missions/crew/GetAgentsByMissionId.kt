package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel

@UseCase
class GetAgentsByMissionId(private val agentRepository: IAgentRepository) {

  fun execute(missionId: Int): List<AgentModel> {
    return agentRepository.findByMissionId(missionId = missionId)
  }
}
