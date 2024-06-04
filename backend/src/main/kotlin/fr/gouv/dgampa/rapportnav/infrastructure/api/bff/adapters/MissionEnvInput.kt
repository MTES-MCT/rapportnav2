package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import java.time.ZonedDateTime

data class MissionEnvInput(
    val missionId: Int,
    val observationsByUnit: String?,
    val startDateTimeUtc: ZonedDateTime?,
    val endDateTimeUtc: ZonedDateTime?,
) {
}

