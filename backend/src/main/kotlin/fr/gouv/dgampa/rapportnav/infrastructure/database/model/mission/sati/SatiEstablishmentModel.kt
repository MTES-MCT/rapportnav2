package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Objects

@Entity
@Table(name = "sati_establishment")
@EntityListeners(AuditingEntityListener::class)
class SatiEstablishmentModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    var id: Int? = null,

    @Column(name = "type", length = 50)
    var type: String? = null,

    @Column(name = "sub_type", length = 50)
    var subType: String? = null,

    @Column(name = "establishment_id")
    var establishmentId: Int? = null,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "establishment_id", referencedColumnName = "id")
    var parties: MutableList<SatiPartyModel> = mutableListOf(),

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id, type, subType, establishmentId, parties)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SatiEstablishmentModel
        return id == other.id
            && type == other.type
            && subType == other.subType
            && establishmentId == other.establishmentId
            && parties == other.parties
    }
}
