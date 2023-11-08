package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew.*
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class CrewController (
    private val getAgents: GetAgents,
    private val getAgentsByServiceId: GetAgentsByServiceId,
    private val getAgentsByMissionId: GetAgentsByMissionId,
    private val getAgentRoles: GetAgentRoles
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
  fun agentsByMissionId(@Argument missionId: Int): List<AgentModel> {
    return  getAgentsByMissionId.execute(
      missionId = missionId
    )
  }

  fun agentRoles(): List<AgentRoleModel>{
      return getAgentRoles.execute()
  }
}
