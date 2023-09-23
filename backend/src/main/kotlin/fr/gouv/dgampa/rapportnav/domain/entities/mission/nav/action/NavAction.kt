package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import java.time.ZonedDateTime

data class NavAction(
    val id: Int?,
    val missionId: Int,
    val actionStartDateTimeUtc: ZonedDateTime,
    val actionEndDateTimeUtc: ZonedDateTime?,
    val actionType: ActionType,
    val actionControl: ActionControlModel? = null,
)
