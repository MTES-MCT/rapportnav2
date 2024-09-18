package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import java.time.Instant
import java.util.*

data class PatchedEnvActionEntity(
    val id: UUID,
    val actionStartDateTimeUtc: Instant? = null,
    val actionEndDateTimeUtc: Instant? = null,
    val observationsByUnit: String? = null,
)
