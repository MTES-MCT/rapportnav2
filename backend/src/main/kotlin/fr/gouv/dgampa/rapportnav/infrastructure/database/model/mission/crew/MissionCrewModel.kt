package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import jakarta.persistence.*

@Entity
@Table(name = "mission_crew")
data class MissionCrewModel(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int?,

  @ManyToOne
  @JoinColumn(name = "agent_id")
  var agent: AgentModel,

  @Column(name = "mission_id")
  var missionId: Int,

  @Column(name = "comment", nullable = true)
  var comment: String?,

  @ManyToOne
  @JoinColumn(name = "agent_role_id")
  var role: AgentRoleModel,


) {

  fun toMissionCrewEntity(): MissionCrewEntity {
    return MissionCrewEntity(
      id = id,
      missionId = missionId,
      agent = agent.toAgentEntity(),
      role = role.toAgentRoleEntity(),
      comment = comment
    )
  }
  companion object {
    fun fromMissionCrewEntity(crew: MissionCrewEntity): MissionCrewModel {
      return MissionCrewModel(
        id = crew.id,
        missionId = crew.missionId,
        agent = AgentModel.fromAgentEntity(crew.agent),
        role = AgentRoleModel.fromAgentRoleEntity(crew.role),
        comment = crew.comment
      )
    }
  }
}
