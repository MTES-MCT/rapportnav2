package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity

data class ServiceEntity(
  val id: Int,
  val name: String,
  val agents: MutableList<AgentEntity>,
  val serviceLinked: ServiceEntity?
)
