package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentCrewRepository
import org.springframework.stereotype.Repository

@Repository
class JPAAgentCrewRepository(
  private val dbCrewRepository: IDBAgentCrewRepository
) : IAgentCrewRepository {
  override fun findByMissionId(missionId: Int): List<AgentCrewModel> {
    return dbCrewRepository.findByMissionId(missionId)
  }
}
