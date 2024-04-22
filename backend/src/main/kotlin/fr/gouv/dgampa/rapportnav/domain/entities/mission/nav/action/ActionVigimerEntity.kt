package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionVigimer
import java.time.ZonedDateTime
import java.util.*

class ActionVigimerEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime,
    val observations: String? = null,
) {
    fun toNavActionVigimer(): NavActionVigimer {
        return NavActionVigimer(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations
        )
    }
}
