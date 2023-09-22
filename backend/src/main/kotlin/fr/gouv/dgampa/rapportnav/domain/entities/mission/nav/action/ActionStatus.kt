package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import java.time.LocalDateTime

data class ActionStatus(
    val id: Int,
    val missionId: Int,
    val startDateTimeUtc: LocalDateTime,
    val status: ActionStatusType,
    val reason: ActionStatusReason?,
    val isStart: Boolean,
    val observations: String?
)
