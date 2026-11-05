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

/** [text] wrapped within the width, left-aligned or [centered]. Takes no room when blank. */
fun PdfContext.paragraph(
    text: String,
    font: PDFont = fonts.regular,
    fontSize: Float = PdfLayout.BODY_FONT_SIZE,
    lineHeight: Float = PdfLayout.BODY_LINE_HEIGHT,
    color: Color = PdfColors.TEXT_DEFAULT_GREY,
    centered: Boolean = false,
): PdfBlock {
    val lines = wrapText(text, font, fontSize, width)
    return PdfBlock(lines.size * lineHeight) { top ->
        lines.forEachIndexed { index, line ->
            val lineX = if (centered) x + (width - textWidth(line, font, fontSize)) / 2 else x
            text(line, lineX, top - fontSize - index * lineHeight, font, fontSize, color)
        }
    }
}
