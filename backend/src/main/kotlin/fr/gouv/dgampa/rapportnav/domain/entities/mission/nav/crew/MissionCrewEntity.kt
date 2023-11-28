package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew


data class MissionCrewEntity(

  val id: Int? = null,
  val agent: AgentEntity,
  val comment: String?,
  val role: AgentRoleEntity,
  val missionId: Int,
)
