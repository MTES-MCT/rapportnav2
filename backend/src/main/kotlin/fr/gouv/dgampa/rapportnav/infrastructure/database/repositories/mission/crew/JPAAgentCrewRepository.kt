package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentCrewEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew.AgentCrewInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentCrewRepository
import jakarta.transaction.Transactional
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository

@Repository
class JPAAgentCrewRepository(
  private val dbCrewRepository: IDBAgentCrewRepository
) : IAgentCrewRepository {
  override fun findByMissionId(missionId: Int): List<AgentCrewModel> {
    return dbCrewRepository.findByMissionId(missionId)
  }

  @Transactional
  override fun save(agentCrew: AgentCrewModel): AgentCrewModel {
    return try {
      dbCrewRepository.save(agentCrew)
    } catch (e: InvalidDataAccessApiUsageException) {
      throw Exception("Error saving or updating administrative Control", e)
    }
  }
}
