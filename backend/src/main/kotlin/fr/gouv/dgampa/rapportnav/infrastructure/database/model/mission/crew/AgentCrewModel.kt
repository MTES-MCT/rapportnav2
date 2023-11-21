package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentCrewEntity
import jakarta.persistence.*

@Entity
@Table(name = "agent_crew")
data class AgentCrewModel(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int?,

  @ManyToOne(cascade = [CascadeType.ALL])
  var agent: AgentModel,

  @Column(name = "mission_id")
  var missionId: Int,

  @Column(name = "comment", nullable = true)
  var comment: String?,

  @ManyToOne
  @JoinColumn(name = "agent_role_id")
  var role: AgentRoleModel,


)
