package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@UseCase
class FormatDateTime {

    fun formatDate(instant: Instant?, zoneId: ZoneId = ZoneId.of("Europe/Paris")): String {
        return instant?.atZone(zoneId)
            ?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            ?: "N/A"
    }

    fun formatTime(instant: Instant?, zoneId: ZoneId = ZoneId.of("Europe/Paris")): String {
        return instant?.atZone(zoneId)
            ?.format(DateTimeFormatter.ofPattern("HH:mm"))
            ?: "N/A"
    }
}
