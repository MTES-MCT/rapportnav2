package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfLayout
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.horizontalLine
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.textWidth
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.wrapText
import org.apache.pdfbox.pdmodel.font.PDFont

/** A row of a checklist: its [label] on the left, its [answer] on the right, and a grey line below. */
fun PdfContext.checkRow(
    label: String,
    answer: String,
    answerFont: PDFont,
    fontSize: Float = PdfLayout.BODY_FONT_SIZE,
    lineHeight: Float = PdfLayout.BODY_LINE_HEIGHT,
): PdfBlock {
    val labelLines = wrapText(label, fonts.regular, fontSize, width - ANSWER_WIDTH)
    val height = 2 * PADDING + (labelLines.size - 1) * lineHeight + fontSize
    return PdfBlock(height) { top ->
        val firstBaseline = top - PADDING - fontSize
        labelLines.forEachIndexed { index, line ->
            text(line, x, firstBaseline - index * lineHeight, fonts.regular, fontSize)
        }
        val answerX = x + width - textWidth(answer, answerFont, fontSize)
        text(answer, answerX, firstBaseline, answerFont, fontSize, PdfColors.TEXT_TITLE_GREY)
        horizontalLine(x, top - height, width, PdfColors.BORDER_DEFAULT_GREY, lineWidth = 0.5f)
    }
}

private const val PADDING = 7f
// room kept on the right for the answer
private const val ANSWER_WIDTH = 60f
