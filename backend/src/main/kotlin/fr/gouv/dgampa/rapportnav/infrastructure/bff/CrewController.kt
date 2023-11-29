package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.crew.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew.MissionCrewInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.crew.Agent
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.crew.AgentRole
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.crew.MissionCrew
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller

@Controller
class CrewController (
  private val getAgents: GetAgents,
  private val getAgentsByServiceId: GetAgentsByServiceId,
  private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
  private val getAgentRoles: GetAgentRoles,
  private val deleteMissionCrew: DeleteMissionCrew,
  private val addOrUpdateAgentCrew: AddOrUpdateMissionCrew
){

  private val logger = LoggerFactory.getLogger(CrewController::class.java)


  @QueryMapping
    fun agents(): List<Agent>{
        return getAgents.execute().map { Agent.fromAgentEntity(it) }
    }

  @QueryMapping
  fun agentsByServiceId(@Argument serviceId: Int): List<Agent>?{
    return try {

       getAgentsByServiceId.execute(
        serviceId = serviceId
      ).map { Agent.fromAgentEntity(it) }
    }
    catch (e: Exception) {
      logger.error("[ERROR] API on endpoint agentsByServiceId :", e)
      null
    }
  }

  @QueryMapping
  fun agentsByUserService(): List<Agent>?{
    return try {
      val authentication = SecurityContextHolder.getContext().authentication
      val authenticatedUser: User = authentication.principal as User

      if (authenticatedUser.agentId != null) {
        getAgentsByServiceId.execute(
          serviceId = authenticatedUser.agentId!!
        ).map { Agent.fromAgentEntity(it) }
      }
      else {
        null
      }
    }
    catch (e: Exception) {
      logger.error("[ERROR] API on endpoint agentsByServiceId :", e)
      null
    }
  }

  @QueryMapping
  fun missionCrewByMissionId(@Argument missionId: Int): List<MissionCrew> {
    return  getAgentsCrewByMissionId.execute(
      missionId = missionId
    ).map { MissionCrew.fromMissionCrewEntity(it) }
  }

  @QueryMapping
  fun agentRoles(): List<AgentRole>{
      return getAgentRoles.execute().map { AgentRole.fromAgentRoleEntity(it) }
  }

  @MutationMapping
  fun addOrUpdateMissionCrew(@Argument crew: MissionCrewInput): MissionCrew {
    val data = crew.toMissionCrewEntity()
    val savedData = addOrUpdateAgentCrew.addOrUpdateMissionCrew(crew = data)
    return MissionCrew.fromMissionCrewEntity(savedData)
  }

  @MutationMapping
  fun deleteMissionCrew(@Argument id: Int): Boolean {
    return deleteMissionCrew.execute(id = id)
  }
}
