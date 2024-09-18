package fr.gouv.gmampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant

@SpringBootTest(classes = [FormatDateTime::class])
class FormatDateTimeTests {

    @Autowired
    private lateinit var formatDateTime: FormatDateTime

    @Test
    fun `formatTime should return formatted time`() {
        assertThat(
            formatDateTime.formatTime(
                Instant.parse("2022-01-02T12:00:00Z")
            )
        ).isEqualTo("13:00")
    }

    @Test
    fun `formatTime should return NA when null`() {
        assertThat(formatDateTime.formatTime(null)).isEqualTo("N/A")
    }

    @Test
    fun `formatDate should return formatted time`() {
        assertThat(
            formatDateTime.formatDate(
                Instant.parse("2022-01-02T12:00:00Z")
            )
        ).isEqualTo("2022-01-02")
    }

    @Test
    fun `formatDate should return NA when null`() {
        assertThat(formatDateTime.formatDate(null)).isEqualTo("N/A")
    }
}
