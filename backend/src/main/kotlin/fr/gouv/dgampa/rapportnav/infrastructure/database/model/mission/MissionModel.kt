package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "mission")
data class MissionModel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int?,

    @Column(name = "mission_types", nullable = false)
    var missionTypes: List<MissionTypeEnum>,

    @Column(name = "control_units", nullable = true)
    var controlUnits: List<Int>,

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

    @Column(name = "mission_source", nullable = true)
    var missionSource: MissionSourceEnum? = null,

    @Column(name = "observations_by_unit", nullable = true)
    var observationsByUnit: String? = null,

    @Column(name = "control_unit_id_owner", nullable = false)
    var controlUnitIdOwner: Int
)
