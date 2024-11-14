package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters

import java.time.Instant

data class MissionsFetchEnvInput(
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null
) {
}
