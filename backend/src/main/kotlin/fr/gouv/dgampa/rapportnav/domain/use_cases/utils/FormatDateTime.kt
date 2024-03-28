package fr.gouv.dgampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.config.UseCase
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@UseCase
class FormatDateTime {
    fun formatTime(dateTime: ZonedDateTime?): String {
        return dateTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "N/A"
    }
}
