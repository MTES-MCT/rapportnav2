package fr.gouv.gmampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@SpringBootTest(classes = [FormatDateTime::class])
class FormatDateTimeTests {

    @Autowired
    private lateinit var formatDateTime: FormatDateTime

    @Test
    fun `formatTime should return formatted time`() {
        assertThat(
            formatDateTime.formatTime(
                ZonedDateTime.of(
                    LocalDateTime.of(2022, 1, 2, 12, 0),
                    ZoneOffset.UTC
                )
            )
        ).isEqualTo("12:00")
    }

    @Test
    fun `formatTime should return NA when null`() {
        assertThat(formatDateTime.formatTime(null)).isEqualTo("N/A")
    }
}
