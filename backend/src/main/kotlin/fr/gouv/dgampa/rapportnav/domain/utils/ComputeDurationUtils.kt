package fr.gouv.dgampa.rapportnav.domain.utils

import java.text.DecimalFormat
import java.time.ZonedDateTime
import kotlin.time.DurationUnit

class ComputeDurationUtils {
    companion object {

        fun durationInSeconds(startDateTimeUtc: ZonedDateTime?, endDateTimeUtc: ZonedDateTime?): Int? {
            if (endDateTimeUtc == null || startDateTimeUtc == null) {
                return null
            }
            val endTime = endDateTimeUtc.toEpochSecond()
            val startTime = startDateTimeUtc.toEpochSecond()
            return (endTime - startTime).toInt()
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

        fun durationInHours(startDateTimeUtc: ZonedDateTime?, endDateTimeUtc: ZonedDateTime?): Double {
            if (endDateTimeUtc == null || startDateTimeUtc == null) {
                return 0.0;
            }
            val duration  = durationInSeconds(startDateTimeUtc, endDateTimeUtc) ?: return 0.0;
            return convertFromSeconds(duration, DurationUnit.HOURS);
        }
    }
}
