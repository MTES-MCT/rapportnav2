package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDate

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "mission_crew_absence")
class MissionCrewAbsenceModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int? = null,

    @Column(name = "start_date", nullable = true)
    var startDate: LocalDate? = null,

    @Column(name = "end_date", nullable = true)
    var endDate: LocalDate? = null,

    @Column(name = "is_absent_full_mission", nullable = true)
    var isAbsentFullMission: Boolean? = null,

    @Column(name = "reason", nullable = true)
    var reason: String? = null,

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    var createdBy: Int? = null,

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: Int? = null,
//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_crew_id", referencedColumnName = "id", nullable = true)
    @JsonIgnore
    var missionCrew: MissionCrewModel? = null,
)
