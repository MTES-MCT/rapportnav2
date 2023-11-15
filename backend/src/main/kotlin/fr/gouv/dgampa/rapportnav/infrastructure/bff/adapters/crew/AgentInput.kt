package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.crew

import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.ServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import java.util.Date

data class AgentInput(
  val id: Int,
  val firstName: String,
  val lastName: String,
  val deletedAt: Date?,
  val service: ServiceInput
) {
  fun toAgentModel(): AgentModel {
    return AgentModel(
      id = id,
      firstName = firstName,
      lastName = lastName,
      deletedAt = deletedAt,
      services = mutableSetOf(service.toServiceModel())
    )
  }
}
