package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import jakarta.persistence.*
import java.util.HashSet

@Entity
@Table(name = "service")
class ServiceModel(

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  var id: Int?,

  @Column(name = "name", unique = true, nullable = false)
  var name: String,

//  @ManyToMany
//  // @JoinTable(
//  //   name = "agent_service",
//  //   joinColumns = [JoinColumn(name = "service_id")],
//  //   inverseJoinColumns = [JoinColumn(name = "agent_id")]
//  // )
//  @ManyToMany(mappedBy = "services", targetEntity = AgentModel::class)
//  // @JsonIgnore
//  // @JsonIgnoreProperties("services")
//  var agents: MutableSet<AgentModel?> = HashSet(),

//  @OneToOne
////  @JsonIgnore
////  @JsonIgnoreProperties("serviceLinked")
//  @JoinColumn(name = "service_linked_id")
//  var serviceLinked: ServiceModel? = null,

  @ElementCollection
  @CollectionTable(
    name = "service_control_unit",
    joinColumns = [JoinColumn(name = "service_id")]
  )
  @Column(name = "control_unit_id")
  var controlUnits: List<Int>? = mutableListOf()

  ) {

  fun toServiceEntity(): ServiceEntity {
    return ServiceEntity(
      id = id,
      name = name,
      controlUnits = controlUnits
//      agents = agents.map { it?.toAgentEntity() }.toMutableSet(),
//      serviceLinked = serviceLinked?.toServiceEntity(),

    )
  }

  companion object {
    fun fromServiceEntity(service: ServiceEntity): ServiceModel {
      return ServiceModel(
        id = service.id,
        name = service.name,
        controlUnits = service.controlUnits
//        agents = service.agents.map { AgentModel.fromAgentEntity(it!!) }.toMutableSet(),
//        serviceLinked = fromServiceEntity(service.serviceLinked!!)
      )
    }
  }
}
