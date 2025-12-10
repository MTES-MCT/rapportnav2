package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "mission_passenger")
class MissionPassengerModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

    @Column(name = "mission_id", nullable = true)
    var missionId: Int? = null,

    @Column(name = "mission_id_uuid", nullable = true)
    var missionIdUUID: UUID? = null,

    @Column(name = "full_name")
    var fullName: String,

    @Column(name = "organization", nullable = true)
    var organization: String? = null,

    @Column(name = "is_intern", nullable = true)
    var isIntern: Boolean? = null,

    @Column(name = "start_date", nullable = false)
    var startDate: LocalDate,

    @Column(name = "end_date", nullable = false)
    var endDate: LocalDate,

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
