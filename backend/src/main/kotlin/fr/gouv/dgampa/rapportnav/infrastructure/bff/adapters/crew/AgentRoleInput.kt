package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel

data class AgentRoleInput(
  val id: Int,
  val title: String
) {
  fun toAgentRoleModel(): AgentRoleModel {
    return AgentRoleModel(
      id = id,
      title = title
    )
  }
}
