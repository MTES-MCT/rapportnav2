package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import java.time.Instant

data class ActionFishInput(
    val missionId: String,
    val actionId: String,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
)
