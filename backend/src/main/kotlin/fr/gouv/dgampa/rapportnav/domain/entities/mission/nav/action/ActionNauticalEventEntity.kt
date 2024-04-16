package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionNauticalEvent
import java.time.ZonedDateTime
import java.util.*

class ActionNauticalEventEntity(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null,
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
}
