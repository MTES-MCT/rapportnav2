package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentCrewRepository

@UseCase
data class DeleteAgentCrew(private val crewRepository: IAgentCrewRepository) {

  fun execute(id: Int): Boolean {

    return try {
      crewRepository.deleteById(
        agentCrewId = id
      )
    } catch (e: NoSuchElementException) {
      // TODO add log
      return false
    }
  }
}
