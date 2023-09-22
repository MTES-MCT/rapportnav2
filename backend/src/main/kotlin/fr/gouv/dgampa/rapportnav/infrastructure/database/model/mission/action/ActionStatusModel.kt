package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.mapStringToActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.mapStringToActionStatusType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "mission_action_status")
data class ActionStatusModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mission_action_status_id_seq")
    @SequenceGenerator(name = "mission_action_status_id_seq", allocationSize = 1)
    @Column(name = "id")
    var id: Int,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: LocalDateTime,

    @Column(name = "status", nullable = false)
    var status: String,

    @Column(name = "reason", nullable = true)
    var reason: String?,

    @Column(name = "is_start", nullable = false)
    var isStart: Boolean,

    @Column(name = "observations", nullable = true)
    var observations: String?,
) {
    fun toActionStatus(): ActionStatus {
        return ActionStatus(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            status = mapStringToActionStatusType(status),
            reason = mapStringToActionStatusReason(reason),
            isStart = isStart,
            observations = observations,
        )
    }
}
