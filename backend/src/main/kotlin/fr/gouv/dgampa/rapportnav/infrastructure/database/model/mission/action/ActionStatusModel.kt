package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.mapStringToActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.mapStringToActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.toStringOrNull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "mission_action_status")
class ActionStatusModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "is_complete_for_stats", nullable = true)
    var isCompleteForStats: Boolean? = null,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: ZonedDateTime,

    @Column(name = "status", nullable = false)
    var status: String,

    @Column(name = "reason", nullable = true)
    var reason: String?,

    @Column(name = "observations", nullable = true)
    var observations: String?,

    ) {
    fun toActionStatusEntity(): ActionStatusEntity {
        return ActionStatusEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            startDateTimeUtc = startDateTimeUtc,
            status = mapStringToActionStatusType(status),
            reason = mapStringToActionStatusReason(reason),
            observations = observations,
        )
    }

    companion object {
        fun fromActionStatusEntity(statusAction: ActionStatusEntity) = ActionStatusModel(
            id = statusAction.id,
            missionId = statusAction.missionId,
            isCompleteForStats = statusAction.isCompleteForStats,
            startDateTimeUtc = statusAction.startDateTimeUtc,
            status = statusAction.status.toString(),
            reason = statusAction.reason.toStringOrNull(),
            observations = statusAction.observations,
        )
    }

}
