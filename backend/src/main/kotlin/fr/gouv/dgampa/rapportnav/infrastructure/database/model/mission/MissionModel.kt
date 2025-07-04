package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "mission")
data class MissionModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "service_id", nullable = false)
    var serviceId: Int,

    @Column(name = "open_by", nullable = true)
    var openBy: String? = null,

    @Column(name = "completed_by", nullable = true)
    var completedBy: String? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: Instant,

    @Column(name = "end_datetime_utc", nullable = true)
    var endDateTimeUtc: Instant? = null,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_source", nullable = true)
    var missionSource: MissionSourceEnum? = null,

    @Column(name = "observations_by_unit", nullable = true)
    var observationsByUnit: String? = null
)
