package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.*

@Table(name = "cross_control")
@Entity
class CrossControlModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    val id: UUID,

    @Column(name = "type", nullable = true)
    val type: String? = null,

    @Column(name = "status", nullable = true)
    val status: String? = null,

    @Column(name = "origin", nullable = true)
    val origin: String? = null,

    @Column(name = "agent_id", nullable = true)
    val agentId: String? = null,

    @Column(name = "vessel_id", nullable = true)
    val vesselId: Int? = null,

    @Column(name = "service_id", nullable = true)
    val serviceId: Int? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: Instant,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: Instant? = null,

    @Column(name = "conclusion", nullable = true)
    val conclusion: String? = null
) {
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CrossControlModel
        return id == other.id
    }
}
