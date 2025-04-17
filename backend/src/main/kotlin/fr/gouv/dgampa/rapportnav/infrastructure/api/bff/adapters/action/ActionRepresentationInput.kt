package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRepresentationEntity
import java.time.Instant
import java.util.*

class ActionRepresentationInput(
    var id: UUID? = null,
    var missionId: String,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var observations: String? = null
) {

    fun toActionRepresentationEntity(): ActionRepresentationEntity {
        return ActionRepresentationEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }
}
