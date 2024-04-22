package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionVigimerEntity
import java.time.ZonedDateTime
import java.util.*

class ActionVigimerInput(
    var id: UUID? = null,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null
) {

    fun toActionVigimerEntity(): ActionVigimerEntity {
        return ActionVigimerEntity(
            id = id?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }
}
