package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

    data class ActionStatusInput(
        val id: UUID?,
        val missionId: Int,
        val startDateTimeUtc: String?,
        val status: ActionStatusType,
        val reason: ActionStatusReason?,
        val isStart: Boolean,
        val observations: String?
    ) {
    fun toActionStatus(): ActionStatusEntity {
        return ActionStatusEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc?.let {
                ZonedDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX"))
            } ?: ZonedDateTime.now(ZoneId.of("UTC")),
            status = status,
            reason = reason,
            isStart = isStart,
            observations = observations
        )
    }
}
