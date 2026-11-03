package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color

/*
 * Text primitives for pdfbox documents.
 * Coordinates are in PDF points, origin at the bottom-left of the page.
 */

fun textWidth(text: String, font: PDFont, fontSize: Float): Float =
    font.getStringWidth(printable(text, font)) / 1000f * fontSize

/** Draws [text] on a single line, starting at the baseline ([x], [y]). */
fun PDPageContentStream.text(text: String, x: Float, y: Float, font: PDFont, fontSize: Float, color: Color = PdfColors.TEXT_DEFAULT_GREY) {
    beginText()
    setFont(font, fontSize)
    setNonStrokingColor(color)
    newLineAtOffset(x, y)
    showText(printable(text, font))
    endText()
}

/**
 * Splits [text] into lines that fit [maxWidth]. The first line is shortened by [firstLineIndent],
 * to leave room for a prefix drawn in another font.
 */
fun wrapText(text: String, font: PDFont, fontSize: Float, maxWidth: Float, firstLineIndent: Float = 0f): List<String> {
    val lines = mutableListOf<String>()
    var line = ""
    for (word in text.split(WHITESPACES).filter { it.isNotEmpty() }) {
        val candidate = if (line.isEmpty()) word else "$line $word"
        val available = if (lines.isEmpty()) maxWidth - firstLineIndent else maxWidth
        if (line.isNotEmpty() && textWidth(candidate, font, fontSize) > available) {
            lines += line
            line = word
        } else {
            line = candidate
        }
    }
    if (line.isNotEmpty()) lines += line
    return lines
}

/**
 * pdfbox throws on characters the font has no glyph for (and on control characters):
 * as texts come from user input, whitespaces become spaces and other unsupported characters "?".
 */
private fun printable(text: String, font: PDFont): String {
    val singleLine = text.replace(CONTROL_WHITESPACES, " ")
    if (canEncode(singleLine, font)) return singleLine
    return singleLine.codePoints().toArray().joinToString("") { codePoint ->
        val character = String(Character.toChars(codePoint))
        if (canEncode(character, font)) character else "?"
    }
}

private fun canEncode(text: String, font: PDFont): Boolean =
    runCatching { font.encode(text) }.isSuccess

private val CONTROL_WHITESPACES = Regex("[\\t\\n\\r\\f]")
private val WHITESPACES = Regex("\\s+")
