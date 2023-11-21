package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import jakarta.persistence.*

@Entity
@Table(name = "service")
data class ServiceModel(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int?,

  @Column(name = "name", unique = true, nullable = false)
  var name: String,

  @ManyToMany
  @JoinTable(
    name = "agent_service",
    joinColumns = [JoinColumn(name = "service_id")],
    inverseJoinColumns = [JoinColumn(name = "agent_id")]
  )
  var agents: MutableSet<AgentModel?> = mutableSetOf(),

  @OneToOne
  @JoinColumn(name = "service_linked_id")
  var serviceLinked: ServiceModel?,

  )
