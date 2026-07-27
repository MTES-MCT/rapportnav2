package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Objects

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

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "vessel_id", referencedColumnName = "id")
    var parties: MutableList<SatiPartyModel> = mutableListOf(),

    @Column(name = "is_master_owner", nullable = true)
    var isMasterOwner: Boolean? = null,

    @Column(name = "port_id", nullable = true)
    var portId: String? = null,

    @Column(name = "last_stop_date", nullable = true)
    var lastStopDate: Instant? = null,

    @Column(name = "last_port_is_not_same", nullable = true)
    var lastPortIsNotSame: Boolean? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id, pnoType, tripNumber, parties, isMasterOwner, portId, lastStopDate)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SatiVesselModel
        return id == other.id
            && pnoType == other.pnoType
            && tripNumber == other.tripNumber
            && parties == other.parties
            && portId == other.portId
            && lastStopDate == other.lastStopDate
            && isMasterOwner == other.isMasterOwner
            && lastPortIsNotSame == other.lastPortIsNotSame
    }
}
