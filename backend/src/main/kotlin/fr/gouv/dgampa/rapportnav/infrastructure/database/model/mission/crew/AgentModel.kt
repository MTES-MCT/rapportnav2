package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "agent")
data class AgentModel (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(name = "deleted_at", nullable = true)
    var deletedAt: Date? = null,

    @ManyToMany(mappedBy = "agents", targetEntity = ServiceModel::class)
    var services: MutableSet<ServiceModel> = HashSet(),
)
