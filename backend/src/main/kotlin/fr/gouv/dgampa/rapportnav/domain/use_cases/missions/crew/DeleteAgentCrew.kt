package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentCrewRepository

@UseCase
data class DeleteAgentCrew(private val crewRepository: IAgentCrewRepository) {

  fun deleteById(id: Int): Boolean {
    return crewRepository.deleteById(
      agentCrewId = id
    )
  }
}
