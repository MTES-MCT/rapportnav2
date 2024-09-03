package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import java.time.Instant
import java.util.*

data class NavActionBAAEMPermanence(
    var id: UUID,
    var missionId: Int,
    var startDateTimeUtc: Instant,
    var endDateTimeUtc: Instant,
    var observations: String? = null,
) : ActionData()
