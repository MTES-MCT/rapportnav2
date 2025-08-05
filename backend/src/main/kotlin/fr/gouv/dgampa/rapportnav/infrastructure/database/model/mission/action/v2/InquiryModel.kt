package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Table(name = "inquiry")
@Entity
@EntityListeners(AuditingEntityListener::class)
class InquiryModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "type", nullable = true)
    var type: String? = null,

    @Column(name = "status", nullable = true)
    var status: String? = null,

    @Column(name = "origin", nullable = true)
    var origin: String? = null,

    @Column(name = "agent_id", nullable = true)
    var agentId: String? = null,

    @Column(name = "vessel_id", nullable = true)
    var vesselId: Int? = null,

    @Column(name = "service_id", nullable = true)
    var serviceId: Int? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: Instant,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: Instant? = null,

    @Column(name = "conclusion", nullable = true)
    var conclusion: String? = null,

    @Column(name = "mission_id", nullable = true)
    var missionId: Int? = null,

    @Column(name = "mission_id_uuid", nullable = true)
    var missionIdUUID: UUID? = null,

    @Column(name = "is_signed_by_inspector", nullable = true)
    var isSignedByInspector: Boolean? = null,

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null

) {
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as InquiryModel
        return id == other.id
    }
}
