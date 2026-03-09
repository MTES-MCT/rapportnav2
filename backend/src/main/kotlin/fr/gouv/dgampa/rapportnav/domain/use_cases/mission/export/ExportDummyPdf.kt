package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import com.lowagie.text.Document
import com.lowagie.text.Font
import com.lowagie.text.FontFactory
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfWriter
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import java.io.ByteArrayOutputStream
import java.util.Base64

@UseCase
class ExportDummyPdf {

    fun execute(): MissionExportEntity {
        val pdfContent = generatePdfContent()
        val base64Content = Base64.getEncoder().encodeToString(pdfContent)

        return MissionExportEntity(
            fileName = "dummy_export.pdf",
            fileContent = base64Content
        )
    }

    private fun generatePdfContent(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val document = Document()

        PdfWriter.getInstance(document, outputStream)
        document.open()

        // Add title
        val titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)
        val title = Paragraph("Rapport de Test PDF", titleFont)
        title.spacingAfter = 20f
        document.add(title)

        // Add description paragraph
        val bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 12f)
        val description = Paragraph(
            "Ce document est un export PDF de test généré par RapportNav. " +
            "Il démontre la capacité de générer des documents PDF avec du texte et des tableaux.",
            bodyFont
        )
        description.spacingAfter = 20f
        document.add(description)

        // Add table with 3 columns
        val table = PdfPTable(3)
        table.widthPercentage = 100f
        table.setSpacingBefore(10f)

        // Header row
        val headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f)
        addCell(table, "Colonne 1", headerFont)
        addCell(table, "Colonne 2", headerFont)
        addCell(table, "Colonne 3", headerFont)

        // Data rows
        val dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11f)
        addCell(table, "Ligne 1, Cellule 1", dataFont)
        addCell(table, "Ligne 1, Cellule 2", dataFont)
        addCell(table, "Ligne 1, Cellule 3", dataFont)

        addCell(table, "Ligne 2, Cellule 1", dataFont)
        addCell(table, "Ligne 2, Cellule 2", dataFont)
        addCell(table, "Ligne 2, Cellule 3", dataFont)

        addCell(table, "Ligne 3, Cellule 1", dataFont)
        addCell(table, "Ligne 3, Cellule 2", dataFont)
        addCell(table, "Ligne 3, Cellule 3", dataFont)

        document.add(table)

        document.close()
        return outputStream.toByteArray()
    }

    private fun addCell(table: PdfPTable, text: String, font: Font) {
        val cell = PdfPCell(Paragraph(text, font))
        cell.setPadding(8f)
        table.addCell(cell)
    }
}
