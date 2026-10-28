package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
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

    @Column(name = "is_not_listed", nullable = false)
    var isNotListed: Boolean = false,

    @Column(name = "is_not_same_address", nullable = false)
    var isNotSameAddress: Boolean = false,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "establishment_id", referencedColumnName = "id")
    var parties: MutableList<SatiPartyModel> = mutableListOf(),

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "address", columnDefinition = "jsonb")
    var address: Map<String, Any?>? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id, type, subType, isNotListed, isNotSameAddress, parties, address)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SatiEstablishmentModel
        return id == other.id
            && type == other.type
            && subType == other.subType
            && isNotListed == other.isNotListed
            && isNotSameAddress == other.isNotSameAddress
            && parties == other.parties
            && address == other.address
    }
}
