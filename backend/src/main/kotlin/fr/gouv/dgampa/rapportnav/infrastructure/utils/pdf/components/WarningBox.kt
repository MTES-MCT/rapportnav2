package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.rect
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.wrapText
import org.apache.pdfbox.pdmodel.PDPageContentStream

/** A yellow box with a warning icon before a wrapped bold [text]. Kept whole. */
fun PdfContext.warningBox(text: String, fontSize: Float = 10f): PdfBlock {
    val lines = wrapText(text, fonts.bold, fontSize, width - 2 * PADDING - ICON_SIZE - ICON_GAP)
    val height = 2 * PADDING + fontSize + (lines.size - 1) * fontSize * 1.5f
    return PdfBlock(height) { top ->
        rect(x, top - height, width, height, PdfColors.BACKGROUND_CONTRAST_YELLOW)

        val firstBaseline = top - PADDING - fontSize
        // icon centered on the first line of text
        warningIcon(x + PADDING, firstBaseline + fontSize * 0.35f - ICON_SIZE / 2, ICON_SIZE)
        lines.forEachIndexed { index, line ->
            text(
                line,
                x + PADDING + ICON_SIZE + ICON_GAP,
                firstBaseline - index * fontSize * 1.5f,
                fonts.bold,
                fontSize,
                PdfColors.TEXT_DEFAULT_YELLOW,
            )
        }
    }
}

/** A filled triangle with an exclamation mark, sitting in a square of [size] whose lower left corner is (x, y). */
private fun PDPageContentStream.warningIcon(x: Float, y: Float, size: Float) {
    setNonStrokingColor(PdfColors.TEXT_DEFAULT_YELLOW)
    moveTo(x + size / 2, y + size)
    lineTo(x + size, y)
    lineTo(x, y)
    closePath()
    fill()

    setNonStrokingColor(PdfColors.BACKGROUND_CONTRAST_YELLOW)
    val barWidth = size * 0.12f
    addRect(x + (size - barWidth) / 2, y + size * 0.32f, barWidth, size * 0.36f)
    addRect(x + (size - barWidth) / 2, y + size * 0.12f, barWidth, barWidth)
    fill()
}

private const val PADDING = 12f
private const val ICON_SIZE = 13f
private const val ICON_GAP = 8f
