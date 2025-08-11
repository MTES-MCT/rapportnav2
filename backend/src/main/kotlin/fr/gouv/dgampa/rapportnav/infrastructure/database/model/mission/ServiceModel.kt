package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "service")
class ServiceModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

    @Column(name = "name", unique = true, nullable = false)
    var name: String,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Int? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Int? = null,

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
