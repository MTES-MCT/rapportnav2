package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.Instant
import java.util.*

data class NavActionPublicOrder(
    var id: UUID,
    var missionId: String,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant? = null,
    var observations: String? = null,
) : ActionData()
