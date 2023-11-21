package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew


data class AgentCrewEntity(

  val id: Int,
  val agent: AgentEntity,
  val comment: String?,
  val role: AgentRoleEntity,
  val missionId: Int,
)
