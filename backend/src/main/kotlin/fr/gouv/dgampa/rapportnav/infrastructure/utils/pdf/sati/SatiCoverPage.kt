package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfLayout.MARGIN
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfLayout.SPACING
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.filledBox
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.labelledText
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.paragraph
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.image
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.MISSING_VALUE
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.formatDate
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.awt.Color

/** Adds the cover page: the logo, the title and the reference of [document], then a summary and a note at the bottom. */
internal fun PdfContext.coverPage(pdf: PDDocument, document: SatiDocumentEntity, logo: ByteArray) {
    val page = PDPage(PDRectangle.A4).also { pdf.addPage(it) }

    PDPageContentStream(pdf, page).use { content ->
        val logoImage = PDImageXObject.createFromByteArray(pdf, logo, "logo")
        content.image(logoImage, x = x, top = page.mediaBox.height - MARGIN, width = LOGO_WIDTH)

        var top = page.mediaBox.height / 2 + 60f
        val title = coverText("Inspection d'un navire de pêche en mer".uppercase(), fonts.bold, 26f, PdfColors.BLUE_FRANCE)
        title.draw(content, top)
        top -= title.height + 12f
        coverText("Rapport d'inspection : ${document.reference}", fonts.regular, 13f, PdfColors.TEXT_MENTION_GREY)
            .draw(content, top)

        // bottom boxes are laid out bottom-up, from the page margin
        var bottom = MARGIN
        val note = filledBox(PdfColors.BACKGROUND_CONTRAST_INFO, accent = PdfColors.BORDER_DEFAULT_INFO) {
            listOf(labelledText("Note :", SatiPdfLabels.NOTE, fontSize = 9f, valueFont = fonts.italic))
        }
        note.draw(content, bottom + note.height)
        bottom += note.height + SPACING
        val summary = filledBox(PdfColors.BACKGROUND_CONTRAST_GREY) {
            listOf(
                "Navire inspecté :" to document.vesselName,
                "Unité en charge de l'inspection :" to document.serviceName,
                "Inspecteur en charge du contrôle :" to document.inspectorName,
                "Date de l'inspection :" to formatDate(document.controlDate),
            ).map { (label, value) -> labelledText(label, value ?: MISSING_VALUE, fontSize = 11f) }
        }
        summary.draw(content, bottom + summary.height)
    }
}

private fun PdfContext.coverText(text: String, font: PDFont, fontSize: Float, color: Color) =
    paragraph(text, font, fontSize, lineHeight = fontSize * 1.3f, color = color, centered = true)

private const val LOGO_WIDTH = 220f
