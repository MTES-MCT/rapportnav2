package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Objects

@Entity
@Table(name = "sati_inspector")
@EntityListeners(AuditingEntityListener::class)
class SatiInspectorModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    var id: Int? = null,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "inspector_id", referencedColumnName = "id")
    var parties: MutableList<SatiPartyModel> = mutableListOf(),

    @Column(name = "authority_type", length = 50)
    var authorityType: String? = null,

    @Column(name = "agent_id")
    var agentId: Int? = null,

    @Column(name = "is_out_of_unit", nullable = false)
    var isOutOfUnit: Boolean = false,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null,
) {
    override fun hashCode(): Int {
        return Objects.hash(id, parties, authorityType, agentId, isOutOfUnit)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SatiInspectorModel
        return id == other.id
            && parties == other.parties
            && authorityType == other.authorityType
            && agentId == other.agentId
            && isOutOfUnit == other.isOutOfUnit
    }
}
