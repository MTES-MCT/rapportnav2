package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import jakarta.persistence.*

@Entity
@Table(name = "service")
data class Service(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int,

  @Column(name = "name", unique = true, nullable = false)
  var name: String,

  @ManyToMany
  @JoinTable(
    name = "service_agent",
    joinColumns = [JoinColumn(name = "service_id")],
    inverseJoinColumns = [JoinColumn(name = "agent_id")]
  )
  var agents: MutableList<AgentModel> = mutableListOf()
)
