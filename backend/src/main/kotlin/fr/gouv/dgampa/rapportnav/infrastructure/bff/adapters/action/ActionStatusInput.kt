package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
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
    fun toActionStatus(): ActionStatusEntity {
        return ActionStatusEntity(
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
