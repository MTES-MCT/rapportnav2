package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import java.security.Provider.Service

@UseCase
class AddOrUpdateCrew(private val crewRepository: IAgentCrewRepository) {

  fun addOrUpdateAgentCrew(agentCrew: AgentCrewModel): AgentCrewModel {
    return crewRepository.save(agentCrew)
  }
}
