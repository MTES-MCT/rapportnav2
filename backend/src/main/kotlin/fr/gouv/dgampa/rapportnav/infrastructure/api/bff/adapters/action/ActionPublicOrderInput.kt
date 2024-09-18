package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionPublicOrderEntity
import java.time.Instant
import java.util.*

class ActionPublicOrderInput(
    var id: UUID? = null,
    var missionId: Int,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant,
    var observations: String? = null
) {

    fun toActionPublicOrderEntity(): ActionPublicOrderEntity {
        return ActionPublicOrderEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }
}
