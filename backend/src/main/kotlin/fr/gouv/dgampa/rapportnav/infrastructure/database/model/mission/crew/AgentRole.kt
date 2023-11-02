package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import jakarta.persistence.*

@Entity
@Table(name = "agent_role")
data class AgentRole(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int,


  @Column(name = "title", unique = true, nullable = false)
  var title: String,
)
