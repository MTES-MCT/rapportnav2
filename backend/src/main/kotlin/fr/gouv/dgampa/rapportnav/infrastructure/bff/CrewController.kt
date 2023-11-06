package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew.*
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class CrewController (
    private val getAgents: GetAgents,
  private val getAgentsByServiceId: GetAgentsByServiceId
){

    @QueryMapping
    fun agents(): List<AgentModel>{
        return getAgents.execute()
    }

  @QueryMapping
  fun agentsByService(): List<AgentModel>{
    return getAgentsByServiceId.execute()
  }
}
