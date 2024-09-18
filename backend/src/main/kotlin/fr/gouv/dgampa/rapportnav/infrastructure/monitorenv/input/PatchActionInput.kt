package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input

import java.time.Instant

data class PatchActionInput(
    val observationsByUnit: String? = null,
    val actionStartDateTimeUtc: Instant? = null,
    val actionEndDateTimeUtc: Instant? = null,
)
