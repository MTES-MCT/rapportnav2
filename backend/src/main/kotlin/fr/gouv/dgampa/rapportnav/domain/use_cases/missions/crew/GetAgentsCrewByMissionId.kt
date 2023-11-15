package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel

@UseCase
class GetAgentsCrewByMissionId(private val agentCrewRepository: IAgentCrewRepository ) {

  fun execute(missionId: Int): List<AgentCrewModel> {
    return agentCrewRepository.findByMissionId(missionId = missionId)
  }
}
