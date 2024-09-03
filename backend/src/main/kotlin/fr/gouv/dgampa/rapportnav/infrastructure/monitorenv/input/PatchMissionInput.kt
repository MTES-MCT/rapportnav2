package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import java.time.Instant

data class PatchMissionInput(
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
)
