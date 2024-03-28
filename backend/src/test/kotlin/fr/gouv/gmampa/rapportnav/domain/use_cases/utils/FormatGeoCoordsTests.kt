package fr.gouv.gmampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatGeoCoords
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [FormatGeoCoords::class])
class FormatGeoCoordsTests {

    @Autowired
    private lateinit var formatGeoCoords: FormatGeoCoords

    @Test
    fun `formatLatLon should return a trimmed lat+lon pair`() {
        val result = formatGeoCoords.formatLatLon(52.1412121, 14.30121212)
        assertThat(result.first).isEqualTo("52.14")
        assertThat(result.second).isEqualTo("14.30")
    }

    @Test
    fun `formatTime should return null when null`() {
        val result = formatGeoCoords.formatLatLon(null, null)
        assertThat(result.first).isNull()
        assertThat(result.second).isNull()
    }
}
