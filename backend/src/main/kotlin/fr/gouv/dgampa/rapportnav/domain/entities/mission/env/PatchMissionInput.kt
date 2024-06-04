package fr.gouv.dgampa.rapportnav.domain.entities.mission.env

import java.time.ZonedDateTime

data class PatchMissionInput(
    val observationsByUnit: String?,
    val startDateTimeUtc: ZonedDateTime?,
    val endDateTimeUtc: ZonedDateTime?,
)
