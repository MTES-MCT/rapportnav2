package fr.gouv.dgampa.rapportnav.domain.entities.mission.env

import java.time.ZonedDateTime

data class PatchMissionInput(
    val observationsByUnit: String? = null,
    val startDateTimeUtc: ZonedDateTime? = null,
    val endDateTimeUtc: ZonedDateTime? = null,
)
