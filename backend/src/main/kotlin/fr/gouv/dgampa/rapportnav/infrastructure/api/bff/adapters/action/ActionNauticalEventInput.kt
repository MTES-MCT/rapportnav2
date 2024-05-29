package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionNauticalEventEntity
import java.time.ZonedDateTime
import java.util.*

class ActionNauticalEventInput(
    var id: UUID? = null,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null
) {

    fun toActionNauticalEventEntity(): ActionNauticalEventEntity {
        return ActionNauticalEventEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }
}
