package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import jakarta.persistence.*

@Entity
@Table(name = "agent_crew")
data class AgentCrewModel(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int,

  @ManyToOne
  var agent: AgentModel,

  @Column(name = "mission_id")
  var missionId: Int,

  @Column(name = "comment", nullable = true)
  var comment: String?,

  @ManyToOne
  @JoinColumn(name = "agent_role_id")
  var role: AgentRoleModel,


)
