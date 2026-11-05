package fr.gouv.gmampa.rapportnav.infrastructure.utils.pdf

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.CoordinatesFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CoordinatesFormatterTest {

    @Test
    fun `should format coordinates in degrees and decimal minutes`() {
        assertThat(CoordinatesFormatter.toDmd(47.80975, -3.909717)).isEqualTo("47° 48.585' N 003° 54.583' W")
    }

    @Test
    fun `should pad minutes and use south and east hemispheres`() {
        assertThat(CoordinatesFormatter.toDmd(-5.1, 120.05)).isEqualTo("05° 06.000' S 120° 03.000' E")
    }

    @Test
    fun `should carry minutes rounded up to 60 over to the degrees`() {
        assertThat(CoordinatesFormatter.toDmd(47.9999999, 0.0)).isEqualTo("48° 00.000' N 000° 00.000'")
    }
}
