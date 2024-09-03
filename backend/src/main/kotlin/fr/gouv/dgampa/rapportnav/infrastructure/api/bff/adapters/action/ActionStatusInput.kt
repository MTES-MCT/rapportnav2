package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.Instant
import java.util.*

data class ActionStatusInput(
    val id: UUID?,
    val missionId: Int,
    val startDateTimeUtc: Instant?,
    val status: ActionStatusType,
    val reason: ActionStatusReason?,
    val observations: String?
) {
    fun toActionStatus(): ActionStatusEntity {

        return ActionStatusEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc ?: Instant.now(),
            status = status,
            reason = reason,
            observations = observations,
        )
    }
}
