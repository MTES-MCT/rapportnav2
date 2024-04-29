package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionNauticalEvent
import java.time.ZonedDateTime
import java.util.*

class ActionNauticalEventEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime,
    val observations: String? = null,
) {
    fun toNavActionNauticalEvent(): NavActionNauticalEvent {
        return NavActionNauticalEvent(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }

    fun toNavAction(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.NAUTICAL_EVENT,
            nauticalEventAction = this
        )
    }
}
