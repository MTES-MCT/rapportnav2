package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action

import java.time.ZonedDateTime
import java.util.*

data class NavActionRepresentation(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: ZonedDateTime,
    var endDateTimeUtc: ZonedDateTime,
    var observations: String? = null,
) : ActionData()
