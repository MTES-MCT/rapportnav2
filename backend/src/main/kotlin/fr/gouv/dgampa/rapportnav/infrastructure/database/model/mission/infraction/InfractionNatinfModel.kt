package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@Table(name = "infraction_natinf")
@IdClass(InfractionNatinfKey::class)
@EntityListeners(AuditingEntityListener::class)
class InfractionNatinfModel(

    @Id
    @Column(name = "infraction_id", nullable = false)
    var infractionId: UUID,

    @Id
    @Column(name = "natinf_code", nullable = false)
    var natinfCode: String,

    @ManyToOne
    @JoinColumn(name = "infraction_id")
    var infraction: InfractionModel? = null,

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
    var updatedBy: Int? = null

)
