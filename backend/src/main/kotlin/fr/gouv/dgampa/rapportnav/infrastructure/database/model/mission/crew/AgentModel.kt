  package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

  import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
  import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
  import jakarta.persistence.*
  import java.util.*

  @Entity
  @Table(name = "agent")
  data class AgentModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: Date? = null,

    @ManyToMany
    @JoinTable(
      name = "agent_service",
      joinColumns = [JoinColumn(name = "agent_id")],
      inverseJoinColumns = [JoinColumn(name = "service_id")]
    )
    var services:  MutableSet<ServiceModel?> = HashSet(),
  ) {

    fun toAgentEntity(): AgentEntity {
      return AgentEntity(
        id = id,
        firstName = firstName,
        lastName = lastName,
        deletedAt = deletedAt,
        )
    }
    companion object {
      fun fromAgentEntity(agent: AgentEntity): AgentModel {
        return AgentModel(
          id = agent.id,
          firstName = agent.firstName,
          lastName = agent.lastName,
          deletedAt = agent.deletedAt,
          services = agent.services.map { ServiceModel.fromServiceEntity(it!!) }.toMutableSet()

        )
      }
    }
  }
