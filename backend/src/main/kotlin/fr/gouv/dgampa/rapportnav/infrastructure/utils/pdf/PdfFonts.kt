package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font

/**
 * Marianne fonts embedded in [PDDocument] (subset to the glyphs used).
 * An embedded font belongs to one document, so they are loaded for each document.
 */
class PdfFonts(val regular: PDFont, val bold: PDFont, val italic: PDFont) {
    companion object {
        fun load(document: PDDocument) = PdfFonts(
            regular = loadFont(document, "/pdf/Marianne-Regular.ttf"),
            bold = loadFont(document, "/pdf/Marianne-Bold.ttf"),
            italic = loadFont(document, "/pdf/Marianne-Regular_Italic.ttf"),
        )

        private fun loadFont(document: PDDocument, path: String): PDFont =
            requireNotNull(PdfFonts::class.java.getResourceAsStream(path)) { "Missing resource $path" }
                .use { PDType0Font.load(document, it) }
    }
}
