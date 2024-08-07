package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import java.time.ZonedDateTime

data class PatchActionInput(
    val observationsByUnit: String? = null,
    val actionStartDateTimeUtc: ZonedDateTime? = null,
    val actionEndDateTimeUtc: ZonedDateTime? = null,
)
