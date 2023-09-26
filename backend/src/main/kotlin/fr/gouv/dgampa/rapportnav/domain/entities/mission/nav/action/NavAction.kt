package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import java.time.ZonedDateTime
import java.util.*

data class NavAction(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val actionType: ActionType,
    val controlAction: ActionControl? = null,
    val statusAction: ActionStatus? = null,
)
