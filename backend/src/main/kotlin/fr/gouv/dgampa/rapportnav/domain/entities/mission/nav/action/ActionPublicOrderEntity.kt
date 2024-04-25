package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionPublicOrder
import java.time.ZonedDateTime
import java.util.*

class ActionPublicOrderEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime,
    val observations: String? = null,
) {
    fun toNavActionPublicOrder(): NavActionPublicOrder {
        return NavActionPublicOrder(
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
            actionType = ActionType.PUBLIC_ORDER,
            publicOrderAction = this
        )
    }
}
