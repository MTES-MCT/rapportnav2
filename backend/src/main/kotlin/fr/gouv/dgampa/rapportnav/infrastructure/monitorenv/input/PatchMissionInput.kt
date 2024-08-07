package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import java.time.ZonedDateTime

data class PatchMissionInput(
    val observationsByUnit: String? = null,
    val startDateTimeUtc: ZonedDateTime? = null,
    val endDateTimeUtc: ZonedDateTime? = null,
)
