package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew.AgentCrewInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class CrewController (
  private val getAgents: GetAgents,
  private val getAgentsByServiceId: GetAgentsByServiceId,
  private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
  private val getAgentRoles: GetAgentRoles,
  private val addOrUpdateAgentCrew: AddOrUpdateCrew,
  private val deleteAgentCrew: DeleteAgentCrew
){

    @QueryMapping
    fun agents(): List<AgentModel>{
        return getAgents.execute()
    }

  @QueryMapping
  fun agentsByServiceId(@Argument serviceId: Int): List<AgentModel>{
    return getAgentsByServiceId.execute(
      serviceId = serviceId
    )
  }

  @QueryMapping
  fun agentCrewByMissionId(@Argument missionId: Int): List<AgentCrewModel> {
    return  getAgentsCrewByMissionId.execute(
      missionId = missionId
    )
  }

  @QueryMapping
  fun agentRoles(): List<AgentRoleModel>{
      return getAgentRoles.execute()
  }

  @MutationMapping
  fun addOrUpdateAgentCrew(@Argument agentCrew: AgentCrewInput): AgentCrewModel {
    val data = agentCrew.toAgentCrewModel()
    return addOrUpdateAgentCrew.addOrUpdateAgentCrew(data)
  }

  @MutationMapping
  fun deleteAgentCrew(@Argument agentCrewId: Int): Boolean
  {
    return deleteAgentCrew.execute(id = agentCrewId)
  }
}
