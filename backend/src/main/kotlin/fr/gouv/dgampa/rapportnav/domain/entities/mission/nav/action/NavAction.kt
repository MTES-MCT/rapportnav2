package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import java.time.LocalDateTime

data class NavAction(
    val id: Int?,
    val missionId: Int,
    val actionStartDateTimeUtc: LocalDateTime,
    val actionEndDateTimeUtc: LocalDateTime?,
    val actionType: ActionType,
    val actionControl: ActionControlModel?,
)
