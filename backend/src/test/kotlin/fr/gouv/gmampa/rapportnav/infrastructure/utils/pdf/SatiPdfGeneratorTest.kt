package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.SatiPdfGenerator
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import org.apache.pdfbox.Loader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SatiPdfGeneratorTest {

    private val generator = SatiPdfGenerator()

    @Test
    fun `generate should return a valid single page pdf`() {
        val bytes = generator.generate(MissionFishActionEntityMock.create(id = 12345))

        assertThat(String(bytes.copyOfRange(0, 4), Charsets.US_ASCII)).isEqualTo("%PDF")
        Loader.loadPDF(bytes).use { document ->
            assertThat(document.numberOfPages).isEqualTo(1)
            assertThat(document.documentInformation.title).isEqualTo("SATI - action 12345")
        }
    }
}
