package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew


import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentCrewRepository
import jakarta.transaction.Transactional
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import java.util.*

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

  @Transactional
  override fun deleteById(agentCrewId: Int): Boolean {
    val agentCrew: Optional<AgentCrewModel> = dbCrewRepository.findById(agentCrewId)
    if (agentCrew.isPresent) {
      dbCrewRepository.deleteById(agentCrewId)
      return true;
    }
    throw NoSuchElementException("AgentCrew with ID $agentCrewId not found")
  }
}
