package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati

import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper

// long texts wrap: compare on a single line
fun pageText(bytes: ByteArray, page: Int): String = Loader.loadPDF(bytes).use { pdf ->
    withoutFooters(PDFTextStripper().apply { startPage = page; endPage = page }.getText(pdf))
}

/** Text of the pages after the cover, where sections flow from page to page. */
fun bodyText(bytes: ByteArray): String = Loader.loadPDF(bytes).use { pdf ->
    withoutFooters(PDFTextStripper().apply { startPage = 2 }.getText(pdf))
}

/** Whitespace normalised, footers left out so that sections read continuously from page to page. */
private fun withoutFooters(text: String): String =
    text.replace(Regex("\\S+ \\d+/\\d+\\s*$", RegexOption.MULTILINE), "").replace(Regex("\\s+"), " ").trim()
