package fr.gouv.dgampa.rapportnav.domain.utils

import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.min
import kotlin.time.DurationUnit

class ComputeDurationUtils {
    companion object {

        fun durationInSeconds(startDateTimeUtc: Instant?, endDateTimeUtc: Instant?): Int? {
            if (endDateTimeUtc == null || startDateTimeUtc == null) {
                return null
            }
            val durationSeconds = Duration.between(startDateTimeUtc, endDateTimeUtc).seconds
            return min(durationSeconds, Int.MAX_VALUE.toLong()).toInt()
        }

        fun convertFromSeconds(durationInSeconds: Int, durationUnit: DurationUnit): Double {
            val result = when (durationUnit) {
                DurationUnit.NANOSECONDS -> (durationInSeconds * 1_000_000_000.0)
                DurationUnit.MICROSECONDS -> (durationInSeconds * 1_000_000.0)
                DurationUnit.MILLISECONDS -> (durationInSeconds * 1_000.0)
                DurationUnit.SECONDS -> durationInSeconds.toDouble()
                DurationUnit.MINUTES -> (durationInSeconds / 60.0)
                DurationUnit.HOURS -> (durationInSeconds / 3600.0)
                DurationUnit.DAYS -> (durationInSeconds / (24.0 * 3600.0))
            }

            val formatter = DecimalFormat("#.##") // Format to two decimal places
            val formattedResult = formatter.format(result).replace(',', '.') // Replace comma with period
            return formattedResult.toDouble()
        }

        fun durationInHours(startDateTimeUtc: Instant?, endDateTimeUtc: Instant?): Double {
            if (endDateTimeUtc == null || startDateTimeUtc == null) {
                return 0.0;
            }
            val duration = durationInSeconds(startDateTimeUtc, endDateTimeUtc) ?: return 0.0;
            return convertFromSeconds(duration, DurationUnit.HOURS);
        }

        fun durationInDays(startDate: LocalDate?, endDate: LocalDate?): Long? {
            if (startDate == null || endDate == null) return null

            val rawDiff = ChronoUnit.DAYS.between(startDate, endDate)

            return when {
                rawDiff == 0L -> 1L
                rawDiff > 0 -> rawDiff + 1
                else -> rawDiff - 1
            }
        }
    }
}
