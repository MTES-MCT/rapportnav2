package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import java.time.ZonedDateTime
import kotlin.time.DurationUnit

@UseCase
class ComputeDurations {
    fun durationInSeconds(startDateTimeUtc: ZonedDateTime, endDateTimeUtc: ZonedDateTime?): Int {
        val startTime = startDateTimeUtc.toEpochSecond()
        val endTime = endDateTimeUtc?.toEpochSecond() ?: ZonedDateTime.now().toEpochSecond()
        return (endTime - startTime).toInt()
    }

    fun convertFromSeconds(durationInSeconds: Int, durationUnit: DurationUnit): Int {
        return when (durationUnit) {
            DurationUnit.SECONDS -> durationInSeconds
            DurationUnit.MINUTES -> durationInSeconds / 60
            DurationUnit.HOURS -> durationInSeconds / 3600
            DurationUnit.NANOSECONDS -> (durationInSeconds * 1_000_000_000)
            DurationUnit.MICROSECONDS -> (durationInSeconds * 1_000_000)
            DurationUnit.MILLISECONDS -> (durationInSeconds * 1_000)
            DurationUnit.DAYS -> durationInSeconds / (24 * 3600)
        }
    }

}
