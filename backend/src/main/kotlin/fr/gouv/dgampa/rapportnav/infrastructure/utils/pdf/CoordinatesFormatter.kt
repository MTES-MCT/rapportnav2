package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

/**
 * Formats coordinates in degrees and decimal minutes (DMD), as monitor-ui does in the app:
 * "47° 48.585' N 003° 54.583' W"
 * (with an apostrophe for minutes: Marianne has no prime glyph).
 */
object CoordinatesFormatter {

    fun toDmd(latitude: Double, longitude: Double): String =
        "${format(latitude, degreesDigits = 2, positive = "N", negative = "S")} " +
            format(longitude, degreesDigits = 3, positive = "E", negative = "W")

    private fun format(value: Double, degreesDigits: Int, positive: String, negative: String): String {
        // work on thousandths of a minute so that rounding up to 60 minutes carries over to the degrees
        val totalMinutes = BigDecimal(abs(value) * 60).setScale(3, RoundingMode.HALF_UP)
        val degrees = totalMinutes.toBigInteger().toInt() / 60
        val minutes = totalMinutes - BigDecimal(degrees * 60)
        val hemisphere = when {
            value > 0 -> " $positive"
            value < 0 -> " $negative"
            else -> ""
        }
        val minutesText = minutes.toPlainString().padStart(6, '0')
        return "${degrees.toString().padStart(degreesDigits, '0')}° $minutesText'$hemisphere"
    }
}
