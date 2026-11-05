package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati

import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/** Formats the values of the SATI pdf. */
internal object SatiPdfFormatter {
    const val MISSING_VALUE = "-"

    private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    private val DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneOffset.UTC)

    /** e.g. "OTB - Chaluts de fond à panneaux", or whichever of both is known. */
    fun codeAndName(code: String?, name: String?): String =
        listOfNotNull(code, name).joinToString(" - ").ifEmpty { MISSING_VALUE }

    // e.g. "988 (kg)"
    fun formatWeight(weight: Double?): String = formatMeasure(weight, "(kg)") ?: MISSING_VALUE

    // French notation, without useless decimals: 12.0 -> "12 m", 12.5 -> "12,5 m"
    fun formatMeasure(value: Double?, unit: String): String? =
        value?.let { "${BigDecimal.valueOf(it).stripTrailingZeros().toPlainString().replace('.', ',')} $unit" }

    fun formatDate(date: LocalDate?): String? = date?.format(DATE_FORMATTER)

    fun formatUtc(instant: Instant?): String? =
        instant?.let { "${DATETIME_FORMATTER.format(it)} (UTC)" }
}
