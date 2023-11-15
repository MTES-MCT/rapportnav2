package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentCrewModel

data class AgentCrewInput(
  val id: Int,
  val agent: AgentInput,
  val missionId: Int,
  val comment: String?,
  val role: AgentRoleInput
) {
  fun toAgentCrewModel(): AgentCrewModel {
    return AgentCrewModel(
      id = id,
      agent = agent.toAgentModel(),
      missionId = missionId,
      comment = comment,
      role = role.toAgentRoleModel()
    )
  }
}
