package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.mapStringToActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.mapStringToActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.toStringOrNull
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "mission_action_status")
data class ActionStatusModel(
    @Id
    @Column(name = "id")
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "start_datetime_utc", nullable = false)
    var startDateTimeUtc: ZonedDateTime,

    @Column(name = "status", nullable = false)
    var status: String,

    @Column(name = "reason", nullable = true)
    var reason: String?,

    @Column(name = "is_start", nullable = false)
    var isStart: Boolean,

    @Column(name = "observations", nullable = true)
    var observations: String?,

    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null,
) {
    fun toActionStatus(): ActionStatusEntity {
        return ActionStatusEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            status = mapStringToActionStatusType(status),
            reason = mapStringToActionStatusReason(reason),
            isStart = isStart,
            observations = observations,
            deletedAt = deletedAt,
        )
    }

    companion object {
        fun fromActionStatus(statusAction: ActionStatusEntity, mapper: ObjectMapper) = ActionStatusModel(
            id = statusAction.id,
            missionId = statusAction.missionId,
            startDateTimeUtc = statusAction.startDateTimeUtc,
            status = statusAction.status.toString(),
            reason = statusAction.reason.toStringOrNull(),
            isStart = statusAction.isStart,
            observations = statusAction.observations,
            deletedAt = statusAction.deletedAt,
        )
    }

}
