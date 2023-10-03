package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction
import java.time.ZonedDateTime
import java.util.*

data class ActionInput(
    val id: UUID,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime,
    val type: ActionType
) {
    fun toNavAction(missionId: Int): NavAction {
        return NavAction(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = type,
        )
    }
}
