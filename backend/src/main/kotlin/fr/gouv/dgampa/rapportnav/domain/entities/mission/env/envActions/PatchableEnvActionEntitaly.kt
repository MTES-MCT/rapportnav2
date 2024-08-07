package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import java.time.ZonedDateTime
import java.util.*

data class PatchedEnvActionEntity(
    val id: UUID,
    val actionStartDateTimeUtc: ZonedDateTime? = null,
    val actionEndDateTimeUtc: ZonedDateTime? = null,
    val observationsByUnit: String? = null,
)
