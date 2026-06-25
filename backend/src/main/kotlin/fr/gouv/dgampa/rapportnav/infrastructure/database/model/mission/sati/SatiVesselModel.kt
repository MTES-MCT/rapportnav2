package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "sati_vessel")
@EntityListeners(AuditingEntityListener::class)
class SatiVesselModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    var id: Int? = null,

    @Column(name = "pno_type", length = 50)
    var pnoType: String? = null,

    @Column(name = "trip_number", length = 50)
    var tripNumber: String? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true, optional = true)
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    var agent: SatiPartyModel? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true, optional = true)
    @JoinColumn(name = "master_id", referencedColumnName = "id")
    var master: SatiPartyModel? = null,

    @Column(name = "is_master_owner", nullable = false)
    var isMasterOwner: Boolean = false,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
)
