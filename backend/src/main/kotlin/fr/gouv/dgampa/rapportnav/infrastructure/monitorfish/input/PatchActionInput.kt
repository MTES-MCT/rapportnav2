package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input

import java.time.ZonedDateTime

data class PatchActionInput(
    val observationsByUnit: String? = null,
    val actionDatetimeUtc: ZonedDateTime? = null,
    val actionEndDatetimeUtc: ZonedDateTime? = null,
)
