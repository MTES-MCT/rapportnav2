package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "infraction_natinf")
@IdClass(InfractionNatinfKey::class)
data class InfractionNatinfModel(

    @Id
    @Column(name = "infraction_id", nullable = false)
    var infractionId: UUID,

    @Id
    @Column(name = "natinf_code", nullable = false)
    var natinfCode: Int,

    @ManyToOne
    @JoinColumn(name = "infraction_id")
    var infraction: InfractionModel? = null

)
