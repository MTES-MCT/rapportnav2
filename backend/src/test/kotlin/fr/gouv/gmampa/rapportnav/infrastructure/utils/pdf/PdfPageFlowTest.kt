package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfPageFlow
import org.apache.pdfbox.pdmodel.PDDocument
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PdfPageFlowTest {

    @Test
    fun `should start on a new page below the top margin`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                assertThat(pdf.numberOfPages).isEqualTo(1)
                assertThat(flow.top).isEqualTo(flow.page.mediaBox.height - 40f)
            }
        }
    }

    @Test
    fun `should stay on the page while the block fits above the bottom margin`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                flow.top = 140f

                flow.ensureSpace(100f)

                assertThat(pdf.numberOfPages).isEqualTo(1)
                assertThat(flow.top).isEqualTo(140f)
            }
        }
    }

    @Test
    fun `should move to a new page when the block does not fit`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                flow.top = 140f

                flow.ensureSpace(101f)

                assertThat(pdf.numberOfPages).isEqualTo(2)
                assertThat(flow.top).isEqualTo(flow.page.mediaBox.height - 40f)
            }
        }
    }

    @Test
    fun `should not leave a blank page for a block taller than a page`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                flow.ensureSpace(flow.page.mediaBox.height * 2)

                assertThat(pdf.numberOfPages).isEqualTo(1)
            }
        }
    }

    @Test
    fun `should split a block taller than a page between its parts, each kept above the bottom margin`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                val partTops = mutableListOf<Pair<Int, Float>>()
                val parts = List(30) { PdfBlock(100f) { top -> partTops += pdf.numberOfPages to top } }

                flow.draw(PdfBlock.stack(parts))

                // 761.89 pt of content per A4 page: 7 parts of 100 pt per page
                assertThat(partTops).hasSize(30)
                assertThat(partTops.map { it.first }.distinct()).containsExactly(1, 2, 3, 4, 5)
                assertThat(partTops).allSatisfy { (_, top) -> assertThat(top - 100f).isGreaterThanOrEqualTo(40f) }
            }
        }
    }

    @Test
    fun `should draw a block made of parts whole when it fits on a page`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                flow.top = 140f
                var drawnTop: Float? = null
                val block = PdfBlock(500f, parts = listOf(PdfBlock(250f) {}, PdfBlock(250f) {})) { top -> drawnTop = top }

                flow.draw(block)

                assertThat(pdf.numberOfPages).isEqualTo(2)
                assertThat(drawnTop).isEqualTo(flow.page.mediaBox.height - 40f)
            }
        }
    }

    @Test
    fun `should keep together a group that fits on a page by moving it to a new page`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                flow.top = 140f

                flow.keepTogether(500f)

                assertThat(pdf.numberOfPages).isEqualTo(2)
            }
        }
    }

    @Test
    fun `should leave a group taller than a page where it is`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                flow.top = 140f

                flow.keepTogether(flow.page.mediaBox.height)

                assertThat(pdf.numberOfPages).isEqualTo(1)
                assertThat(flow.top).isEqualTo(140f)
            }
        }
    }

    @Test
    fun `should tell whether something was laid out on the page`() {
        PDDocument().use { pdf ->
            PdfPageFlow(pdf, margin = 40f).use { flow ->
                assertThat(flow.isAtPageTop).isTrue()

                flow.top -= 10f
                assertThat(flow.isAtPageTop).isFalse()

                flow.newPage()
                assertThat(flow.isAtPageTop).isTrue()
            }
        }
    }
}
