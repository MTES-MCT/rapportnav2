package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionBAAEMPermanenceEntity
import java.time.Instant
import java.util.*

class ActionBAAEMPermanenceInput(
    var id: UUID? = null,
    var missionId: Int,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant,
    var observations: String? = null
) {

    fun toActionBAAEMPermanenceEntity(): ActionBAAEMPermanenceEntity {
        return ActionBAAEMPermanenceEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }

}
