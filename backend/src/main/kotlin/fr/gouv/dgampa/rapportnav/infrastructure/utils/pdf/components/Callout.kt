package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.rect
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.wrapText
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color

/**
 * A DSFR-like callout: a blue box with a left accent border, a bold [title] and wrapped [paragraphs].
 * Made of a part per line, each drawing its slice of the box, so that a callout taller than a page is split between its lines.
 */
fun PdfContext.callout(title: String, paragraphs: List<String>, fontSize: Float = 10f): PdfBlock {
    val lines = wrap(title, fonts.bold, fontSize).map { CalloutLine(it, fonts.bold, PdfColors.TEXT_TITLE_GREY) } +
        paragraphs.flatMap { paragraph ->
            wrap(paragraph, fonts.regular, fontSize).mapIndexed { index, line ->
                CalloutLine(line, fonts.regular, PdfColors.TEXT_DEFAULT_GREY, gapBefore = if (index == 0) TITLE_GAP else 0f)
            }
        }
    val textX = x + ACCENT_WIDTH + PADDING
    return PdfBlock.stack(
        lines.mapIndexed { index, line ->
            val paddingTop = if (index == 0) PADDING else 0f
            val paddingBottom = if (index == lines.lastIndex) PADDING else 0f
            val height = paddingTop + line.gapBefore + fontSize * 1.5f + paddingBottom
            PdfBlock(height) { top ->
                rect(x, top - height, width, height, PdfColors.BACKGROUND_CONTRAST_INFO)
                rect(x, top - height, ACCENT_WIDTH, height, PdfColors.BORDER_DEFAULT_INFO)
                text(line.text, textX, top - paddingTop - line.gapBefore - fontSize, line.font, fontSize, line.color)
            }
        }
    )
}

private class CalloutLine(val text: String, val font: PDFont, val color: Color, val gapBefore: Float = 0f)

private fun PdfContext.wrap(text: String, font: PDFont, fontSize: Float): List<String> =
    wrapText(text, font, fontSize, width - ACCENT_WIDTH - 2 * PADDING)

private const val PADDING = 12f
private const val ACCENT_WIDTH = 4f
private const val TITLE_GAP = 4f
