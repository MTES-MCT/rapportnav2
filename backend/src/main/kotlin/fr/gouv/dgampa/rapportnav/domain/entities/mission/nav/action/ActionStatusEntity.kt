package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.NavActionStatus
import java.time.ZonedDateTime
import java.util.*

data class ActionStatusEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val status: ActionStatusType,
    val reason: ActionStatusReason?,
    val isStart: Boolean,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toNavAction(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = null,
            actionType = ActionType.STATUS,
            statusAction = this
        )
    }
    fun toNavActionStatus(): NavActionStatus {
        return NavActionStatus(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            status = status,
            reason = reason,
            isStart = isStart,
            observations = observations,

        )
    }
}