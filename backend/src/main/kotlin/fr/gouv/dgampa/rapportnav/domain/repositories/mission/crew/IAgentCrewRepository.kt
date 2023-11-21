package fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentCrewEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel

interface IAgentCrewRepository {

  fun findByMissionId(missionId: Int): List<AgentCrewModel>

  fun save(agentCrew: AgentCrewModel): AgentCrewModel
}
