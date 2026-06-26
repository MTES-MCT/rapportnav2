package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Objects
import java.util.UUID

@Entity
@Table(name = "sati")
@EntityListeners(AuditingEntityListener::class)
class SatiModel(

    @Id
    @Column(nullable = false, updatable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "module", nullable = false, length = 50)
    var module: String,

    @Column(name = "action_id", unique = true, nullable = false, length = 255)
    var actionId: String,

    @Column(name = "resource_id")
    var resourceId: Int? = null,

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        optional = true
    )
    @JoinColumn(name = "vessel_id", referencedColumnName = "id")
    var vessel: SatiVesselModel? = null,

    @JsonIgnore
    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @JoinColumn(name = "sati_id", referencedColumnName = "id", nullable = false)
    var inspectors: MutableList<SatiInspectorModel> = mutableListOf(),

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id, module, actionId, resourceId, vessel, inspectors)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SatiModel
        return id == other.id
            && module == other.module
            && actionId == other.actionId
            && resourceId == other.resourceId
            && vessel == other.vessel
            && inspectors == other.inspectors
    }
}
