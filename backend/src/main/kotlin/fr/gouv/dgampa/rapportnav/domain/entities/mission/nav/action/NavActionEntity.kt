package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import java.time.ZonedDateTime
import java.util.*

data class NavActionEntity(
    val id: UUID,
    val missionId: Int,
    var isCompleteForStats: Boolean? = null,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val actionType: ActionType,
    val controlAction: ActionControlEntity? = null,
    val statusAction: ActionStatusEntity? = null,
    val freeNoteAction: ActionFreeNoteEntity? = null
) {

}
