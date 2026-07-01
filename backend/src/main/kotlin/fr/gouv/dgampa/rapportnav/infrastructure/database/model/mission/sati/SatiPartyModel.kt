package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Objects

@Entity
@Table(name = "sati_party")
@EntityListeners(AuditingEntityListener::class)
class SatiPartyModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    var id: Int? = null,

    @Column(name = "party_type", length = 50)
    var partyType: String? = null,

    @Column(name = "comments")
    var comments: String? = null,

    @Column(name = "signature", nullable = false)
    var signature: Boolean = false,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "party_id", referencedColumnName = "id")
    var contacts: MutableList<ContactModel> = mutableListOf(),

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id, partyType, comments, signature, contacts)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SatiPartyModel
        return id == other.id
            && partyType == other.partyType
            && comments == other.comments
            && signature == other.signature
            && contacts == other.contacts
    }
}
