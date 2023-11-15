package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel
import org.springframework.data.jpa.repository.JpaRepository

interface IDBAgentCrewRepository: JpaRepository<AgentCrewModel, Int> {
  fun findByMissionId(missionId: Int): List<AgentCrewModel>
}
