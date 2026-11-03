package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfFonts
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.PdfTag
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.borderedBox
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.callout
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.checkRow
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.columns
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.filledBox
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.labelledText
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.paragraph
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.sectionTitle
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.subsection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.table
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.tags
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.warningBox
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.TextPosition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class PdfComponentsTest {

    private val longText = List(60) { "lorem$it" }.joinToString(" ")

    /** Every component, with texts long enough to wrap. */
    private val components: Map<String, PdfContext.() -> PdfBlock> = mapOf(
        "sectionTitle" to { sectionTitle("Informations sur le contrôle") },
        "subsection" to { subsection("Inspecteur", listOf(paragraph(longText))) },
        "paragraph" to { paragraph(longText) },
        "centered paragraph" to { paragraph(longText, fontSize = 26f, lineHeight = 26f * 1.3f, centered = true) },
        "labelledText" to { labelledText("Adresse :", longText) },
        "columns" to { columns(listOf("Nom", "Prénom", "Adresse")) { labelledText("$it :", longText) } },
        "checkRow" to { checkRow(longText, "NON", fonts.bold) },
        "callout" to { callout("Observations", listOf(longText, longText)) },
        "warningBox" to { warningBox(longText) },
        "table" to { table(listOf("Espèce", "Qté"), List(2) { listOf(longText, "12 (kg)") }) },
        "tags" to { tags(List(12) { PdfTag("Appréhension espèces $it", PdfColors.ERROR) }) },
        "filledBox" to { filledBox(PdfColors.BACKGROUND_CONTRAST_INFO, accent = PdfColors.BORDER_DEFAULT_INFO) { listOf(paragraph(longText)) } },
        "borderedBox" to { borderedBox { listOf(paragraph(longText), PdfBlock.space(8f), tags(listOf(PdfTag("En attente")))) } },
    )

    @Test
    fun `components should draw their text within the height they measure`() {
        components.forEach { (name, build) ->
            val (block, baselines) = drawAlone(build)

            assertThat(baselines).`as`(name).isNotEmpty()
            assertThat(baselines).`as`(name).allSatisfy { baseline ->
                assertThat(baseline).isLessThanOrEqualTo(TOP).isGreaterThanOrEqualTo(TOP - block.height)
            }
        }
    }

    @Test
    fun `table should be split into a part per row, the header staying with the first row`() {
        val table = build { table(listOf("Espèce", "Qté"), List(3) { listOf("HKE", "12 (kg)") }) }

        assertThat(table.parts).hasSize(3)
        assertThat(table.parts.first().parts).hasSize(2)
        assertThat(table.height).isEqualTo(table.parts.sumOf { it.height.toDouble() }.toFloat())
    }

    @Test
    fun `callout should be split into a part per line`() {
        val callout = build { callout("Observations", listOf("Première.", "Seconde.")) }

        assertThat(callout.parts).hasSize(3)
    }

    @Test
    fun `tags should go to the next line when they do not fit`() {
        val oneLine = build { tags(listOf(PdfTag("Sans infraction"))) }
        val twoLines = build { inset(0f, right = width - 150f).tags(listOf(PdfTag("Avec infraction"), PdfTag("Sans appréhension"))) }

        assertThat(twoLines.height).isGreaterThan(2 * oneLine.height)
    }

    @Test
    fun `columns should be as tall as the tallest column`() {
        val short = build { labelledText("Nom :", "Dupont") }
        val tall = build { columns(listOf("Dupont", longText)) { labelledText("Nom :", it) } }

        assertThat(tall.height).isGreaterThan(short.height)
        assertThat(build { columns(listOf("Dupont", "Jean")) { labelledText("Nom :", it) } }.height).isEqualTo(short.height)
    }

    @Test
    fun `paragraph should take no room when blank`() {
        assertThat(build { paragraph(" ") }.height).isZero()
    }

    private fun build(build: PdfContext.() -> PdfBlock): PdfBlock = PDDocument().use { pdf -> PdfContext(PdfFonts.load(pdf)).build() }

    /** The block drawn alone at [TOP] of a page, and the baselines of its text (from the bottom of the page). */
    private fun drawAlone(build: PdfContext.() -> PdfBlock): Pair<PdfBlock, List<Float>> {
        val (block, bytes) = PDDocument().use { pdf ->
            val block = PdfContext(PdfFonts.load(pdf)).build()
            val page = PDPage(PDRectangle.A4).also { pdf.addPage(it) }
            PDPageContentStream(pdf, page).use { block.draw(it, TOP) }
            block to ByteArrayOutputStream().use { output -> pdf.save(output); output.toByteArray() }
        }
        val baselines = mutableListOf<Float>()
        Loader.loadPDF(bytes).use { pdf ->
            object : PDFTextStripper() {
                override fun writeString(text: String, textPositions: List<TextPosition>) {
                    baselines += textPositions.map { currentPage.mediaBox.height - it.yDirAdj }
                }
            }.getText(pdf)
        }
        return block to baselines
    }

    companion object {
        private const val TOP = 800f
    }
}
