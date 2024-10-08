package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils
import java.time.Instant
import kotlin.time.DurationUnit

@UseCase
class ComputeDurations {

    fun durationInSeconds(startDateTimeUtc: Instant?, endDateTimeUtc: Instant?): Int? {
        return ComputeDurationUtils.durationInSeconds(
            startDateTimeUtc,
            endDateTimeUtc
        );
    }

    fun convertFromSeconds(durationInSeconds: Int, durationUnit: DurationUnit): Double {
        return ComputeDurationUtils.convertFromSeconds(durationInSeconds, durationUnit);
    }
}
