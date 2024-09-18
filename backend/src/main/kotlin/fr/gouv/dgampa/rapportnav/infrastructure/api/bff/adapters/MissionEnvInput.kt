package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import java.time.Instant

data class MissionEnvInput(
    val missionId: Int,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
) {
}

