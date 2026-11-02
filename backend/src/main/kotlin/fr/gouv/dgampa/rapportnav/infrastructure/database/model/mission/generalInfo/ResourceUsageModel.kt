package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

/**
 * Per-resource usage value entered in the ULAM general information ("Km parcourus" / "Nb d'heures moteur").
 * Keyed by the mission UUID (mission.id) + the MonitorEnv resource id. Only one of [nbKms] / [nbEngineHours]
 * is set, driven by the resource type. Stored in our DB because MonitorEnv can't hold these values.
 */
@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "mission_resource_usage")
class ResourceUsageModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "mission_id_uuid", nullable = false)
    var missionIdUUID: UUID,

    @Column(name = "resource_id", nullable = false)
    var resourceId: Int = 0,

    @Column(name = "nb_kms", nullable = true)
    var nbKms: Double? = null,

    @Column(name = "nb_engine_hours", nullable = true)
    var nbEngineHours: Double? = null,

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
)
