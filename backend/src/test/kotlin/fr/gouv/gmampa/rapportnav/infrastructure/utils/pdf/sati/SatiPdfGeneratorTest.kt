package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfGenerator
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.text.PDFTextStripper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SatiPdfGeneratorTest {

    private val generator = SatiPdfGenerator()

    @Test
    fun `generate should add a footer with the reference and the page counter on every page but the cover`() {
        Loader.loadPDF(generator.generate(satiDocument)).use { pdf ->
            val lastLines = (1..pdf.numberOfPages).map { page ->
                PDFTextStripper().apply { startPage = page; endPage = page }.getText(pdf).trim().lines().last()
            }

            assertThat(lastLines.first()).doesNotContain("1/${pdf.numberOfPages}")
            lastLines.drop(1).forEachIndexed { index, line ->
                assertThat(line).isEqualTo("${satiDocument.reference} ${index + 2}/${pdf.numberOfPages}")
            }
        }
    }

    @Test
    fun `generate should return a valid pdf titled with the report reference`() {
        val bytes = generator.generate(satiDocument)

        assertThat(String(bytes.copyOfRange(0, 4), Charsets.US_ASCII)).isEqualTo("%PDF")
        Loader.loadPDF(bytes).use { pdf ->
            // a cover, then the sections over as many pages as they need
            assertThat(pdf.numberOfPages).isGreaterThan(1)
            assertThat(pdf.documentInformation.title).isEqualTo("M1_ULAM_56_FLEUR_DE_LYS_2026-12-31")
        }
    }

    @Test
    fun `generate should render the cover page`() {
        val bytes = generator.generate(satiDocument)

        Loader.loadPDF(bytes).use { pdf ->
            val resources = pdf.getPage(0).resources
            val images = resources.xObjectNames.map { resources.getXObject(it) }.filterIsInstance<PDImageXObject>()
            assertThat(images).hasSize(1)

            assertThat(pageText(bytes, page = 1)).contains(
                "INSPECTION D'UN NAVIRE DE PÊCHE EN MER",
                "Rapport d'inspection : M1_ULAM_56_FLEUR_DE_LYS_2026-12-31",
                "Navire inspecté : FLEUR DE LYS",
                "Unité en charge de l'inspection : ULAM 56",
                "Inspecteur en charge du contrôle : Jean Dupont",
                "Date de l'inspection : 31/12/2026",
                "Note :",
                "procès-verbal d'infraction",
            )
        }
    }

    @Test
    fun `generate should render a dash for missing values on the cover page`() {
        val bytes = generator.generate(satiDocument.copy(inspectorFirstName = null, inspectorLastName = null))

        assertThat(pageText(bytes, page = 1)).contains("Inspecteur en charge du contrôle : -")
    }

    @Test
    fun `generate should not fail on user texts the font cannot render`() {
        val bytes = generator.generate(satiDocument.copy(vesselName = "ÉTOILE ★\nDU NORD", serviceName = "ULAM\t56"))

        assertThat(pageText(bytes, page = 1)).contains("Navire inspecté : ÉTOILE ? DU NORD", "Unité en charge de l'inspection : ULAM 56")
    }

    @Test
    fun `generate should embed the Marianne fonts`() {
        val bytes = generator.generate(satiDocument)

        Loader.loadPDF(bytes).use { pdf ->
            val resources = pdf.getPage(0).resources
            val fonts = resources.fontNames.map { resources.getFont(it) }
            assertThat(fonts).allMatch { it.isEmbedded }
            // embedded subsets are prefixed with a random tag, e.g. "ABCDEF+Marianne-Bold"
            assertThat(fonts.map { it.name.substringAfter('+') })
                .containsExactlyInAnyOrder("Marianne-Regular", "Marianne-Bold", "Marianne-RegularItalic")
        }
    }
}
