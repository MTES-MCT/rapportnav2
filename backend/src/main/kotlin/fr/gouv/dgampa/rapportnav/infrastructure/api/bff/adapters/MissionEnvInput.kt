package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import java.time.ZonedDateTime

data class MissionEnvInput(
    val missionId: Int,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: ZonedDateTime? = null,
    val endDateTimeUtc: ZonedDateTime? = null,
) {
}

