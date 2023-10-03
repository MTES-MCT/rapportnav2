package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class ActionStatusInput(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: String,
    val status: ActionStatusType,
    val reason: ActionStatusReason?,
    val isStart: Boolean,
    val observations: String?
) {
    fun toActionStatus(): ActionStatus {
        return ActionStatus(
            id = id,
            missionId = missionId,
            startDateTimeUtc = ZonedDateTime.parse(startDateTimeUtc, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"))),
            status = status,
            reason = reason,
            isStart = isStart,
            observations = observations
        )
    }
}
