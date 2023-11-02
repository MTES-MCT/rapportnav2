package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.Agent
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "crew")
data class CrewModel (

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int,

  @ManyToOne
  @JoinColumn(name = "crew_id")
  var agent: AgentModel,

  @Column(name = "comment", nullable = true)
  var comment: String?,

  @ManyToOne
  @JoinColumn(name = "agent_role_id")
  var role: AgentRole,

  @Column(name = "mission_id")
  var missionId: Int

)
