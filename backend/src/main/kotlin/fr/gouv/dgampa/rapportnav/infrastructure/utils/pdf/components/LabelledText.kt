package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfLayout
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.textWidth
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.wrapText
import org.apache.pdfbox.pdmodel.font.PDFont
import java.awt.Color

/** "[label] [value]", the value wrapping within the width. By default the label is bold and the value regular. */
fun PdfContext.labelledText(
    label: String,
    value: String,
    fontSize: Float = PdfLayout.BODY_FONT_SIZE,
    labelFont: PDFont = fonts.bold,
    labelColor: Color = PdfColors.TEXT_DEFAULT_GREY,
    valueFont: PDFont = fonts.regular,
    lineHeight: Float = fontSize * 1.5f,
): PdfBlock {
    val labelWidth = textWidth("$label ", labelFont, fontSize)
    val lines = wrapText(value, valueFont, fontSize, width, firstLineIndent = labelWidth)
    return PdfBlock(maxOf(lines.size, 1) * lineHeight) { top ->
        val baseline = top - fontSize
        text(label, x, baseline, labelFont, fontSize, labelColor)
        lines.forEachIndexed { index, line ->
            val lineX = if (index == 0) x + labelWidth else x
            text(line, lineX, baseline - index * lineHeight, valueFont, fontSize)
        }
    }
}

/** A block per item side by side, in columns of equal width [gap] apart, as tall as the tallest. */
fun <T> PdfContext.columns(items: List<T>, gap: Float = COLUMN_GAP, column: PdfContext.(T) -> PdfBlock): PdfBlock {
    val blocks = splitColumns(items.size, gap).zip(items) { context, item -> context.column(item) }
    return PdfBlock(blocks.maxOf { it.height }) { top ->
        blocks.forEach { it.draw(this, top) }
    }
}

private const val COLUMN_GAP = 16f
