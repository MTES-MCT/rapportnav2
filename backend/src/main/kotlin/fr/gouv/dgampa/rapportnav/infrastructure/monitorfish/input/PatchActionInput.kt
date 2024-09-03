package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input

import java.time.Instant

data class PatchActionInput(
    val observationsByUnit: String? = null,
    val actionDatetimeUtc: Instant? = null,
    val actionEndDatetimeUtc: Instant? = null,
)
