package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfLayout
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.textWidth
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPageContentStream

/**
 * On every page of [pdf] but the first [skippedPages], [label] on the left and the page number "X/Y" on the right.
 * To be drawn once all pages are laid out, for the page count.
 */
fun PdfContext.footers(
    pdf: PDDocument,
    label: String,
    skippedPages: Int = 0,
    baseline: Float = PdfLayout.MARGIN / 2,
    fontSize: Float = 8f,
) {
    val pageCount = pdf.numberOfPages
    pdf.pages.drop(skippedPages).forEachIndexed { index, page ->
        PDPageContentStream(pdf, page, PDPageContentStream.AppendMode.APPEND, true, true).use { content ->
            content.text(label, x, baseline, fonts.regular, fontSize, PdfColors.TEXT_MENTION_GREY)
            val counter = "${index + skippedPages + 1}/$pageCount"
            val counterX = x + width - textWidth(counter, fonts.regular, fontSize)
            content.text(counter, counterX, baseline, fonts.regular, fontSize, PdfColors.TEXT_MENTION_GREY)
        }
    }
}
