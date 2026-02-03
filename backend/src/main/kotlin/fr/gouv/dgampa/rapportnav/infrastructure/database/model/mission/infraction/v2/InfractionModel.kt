package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.v2

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "infraction_2")
data class InfractionModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "infraction_type", nullable = false)
    var infractionType: String? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @ElementCollection
    @CollectionTable(
        name = "infraction_natinf_2",
        joinColumns = [JoinColumn(name = "infraction_id")]
    )
    @Column(name = "natinf_code")
    var natinfs: List<String> = mutableListOf(),

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
) {
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as InfractionModel
        return id == other.id
    }
}
