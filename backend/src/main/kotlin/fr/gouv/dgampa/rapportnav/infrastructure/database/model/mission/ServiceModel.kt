package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
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
  var serviceLinked: ServiceModel? = null,

  ) {

  fun toServiceEntity(): ServiceEntity {
    return ServiceEntity(
      id = id,
      name = name,
      agents = agents.map { it?.toAgentEntity() }.toMutableSet(),
      serviceLinked = serviceLinked?.toServiceEntity()
    )
  }

  companion object {
    fun fromServiceEntity(service: ServiceEntity): ServiceModel {
      return ServiceModel(
        id = service.id,
        name = service.name,
        agents = service.agents.map { AgentModel.fromAgentEntity(it!!) }.toMutableSet(),
        serviceLinked = fromServiceEntity(service.serviceLinked!!)
      )
    }
  }
}
