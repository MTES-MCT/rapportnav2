package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf.sati

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

class SatiPdfFormatterTest {

    @Test
    fun `codeAndName should join the code and the name, or keep whichever is known`() {
        assertThat(SatiPdfFormatter.codeAndName("OTB", "Chaluts de fond")).isEqualTo("OTB - Chaluts de fond")
        assertThat(SatiPdfFormatter.codeAndName("OTB", null)).isEqualTo("OTB")
        assertThat(SatiPdfFormatter.codeAndName(null, "Chaluts de fond")).isEqualTo("Chaluts de fond")
        assertThat(SatiPdfFormatter.codeAndName(null, null)).isEqualTo("-")
    }

    @Test
    fun `formatMeasure should use the French notation without useless decimals`() {
        assertThat(SatiPdfFormatter.formatMeasure(12.0, "m")).isEqualTo("12 m")
        assertThat(SatiPdfFormatter.formatMeasure(12.5, "m")).isEqualTo("12,5 m")
        assertThat(SatiPdfFormatter.formatMeasure(1000.0, "mm")).isEqualTo("1000 mm")
        assertThat(SatiPdfFormatter.formatMeasure(null, "m")).isNull()
    }

    @Test
    fun `formatWeight should render a weight in kg, or a dash when unknown`() {
        assertThat(SatiPdfFormatter.formatWeight(988.0)).isEqualTo("988 (kg)")
        assertThat(SatiPdfFormatter.formatWeight(1012.5)).isEqualTo("1012,5 (kg)")
        assertThat(SatiPdfFormatter.formatWeight(null)).isEqualTo("-")
    }

    @Test
    fun `formatDate and formatUtc should render French dates`() {
        assertThat(SatiPdfFormatter.formatDate(LocalDate.of(2026, 12, 31))).isEqualTo("31/12/2026")
        assertThat(SatiPdfFormatter.formatDate(null)).isNull()
        assertThat(SatiPdfFormatter.formatUtc(Instant.parse("2026-12-31T23:15:00Z"))).isEqualTo("31/12/2026 23:15 (UTC)")
        assertThat(SatiPdfFormatter.formatUtc(null)).isNull()
    }
}
