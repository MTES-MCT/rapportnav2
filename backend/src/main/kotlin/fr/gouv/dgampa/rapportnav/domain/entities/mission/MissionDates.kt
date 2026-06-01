package fr.gouv.dgampa.rapportnav.domain.entities.mission

import java.time.Instant

data class MissionDates(
    val startDateTimeUtc: Instant?,
    val endDateTimeUtc: Instant?
) {
    /**
     * Returns true if the mission is finished (end date is in the past).
     */
    fun isMissionFinished(): Boolean = endDateTimeUtc?.isBefore(Instant.now()) ?: false
}
