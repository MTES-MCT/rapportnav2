package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.ZonedDateTime
import java.util.*

data class NavActionPublicOrder(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null,
) : ActionData()
