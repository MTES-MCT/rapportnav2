package fr.gouv.gmampa.rapportnav.domain.use_cases.utils

import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.time.DurationUnit

@SpringBootTest(classes = [ComputeDurations::class])
class ComputeDurationsTests {

    @Autowired
    private lateinit var computeDurations: ComputeDurations

    private val startDateTime = ZonedDateTime.of(
        LocalDateTime.of(2022, 1, 2, 12, 0),
        ZoneOffset.UTC
    )

    @Test
    fun `formatTime should return NA when startDate is null`() {
        assertThat(
            computeDurations.durationInSeconds(
                null,
                startDateTime.plusHours(2L)
            )
        ).isNull()
    }

    @Test
    fun `formatTime should return NA when endDate is null`() {
        assertThat(
            computeDurations.durationInSeconds(
                startDateTime,
                null
            )
        ).isNull()
    }

    @Test
    fun `durationInSeconds should return 7200s (=2h) on the same day`() {
        assertThat(
            computeDurations.durationInSeconds(
                startDateTime,
                startDateTime.plusHours(2L)
            )
        ).isEqualTo(7200)
    }

    @Test
    fun `durationInSeconds should return 7200s (=2h) across two days`() {
        assertThat(
            computeDurations.durationInSeconds(
                startDateTime.plusHours(11L), // 11pm
                startDateTime.plusHours(13L), // 1am on the next day
            )
        ).isEqualTo(7200)
    }

    @Test
    fun `durationInSeconds should return negative 7200s (=2h) when end is before start`() {
        assertThat(
            computeDurations.durationInSeconds(
                startDateTime,
                startDateTime.minusHours(2L), // 1am on the next day
            )
        ).isEqualTo(-7200)
    }

    @Test
    fun `durationInSeconds should return 7200s (=12 days ~= length of a mission)`() {
        assertThat(
            computeDurations.durationInSeconds(
                startDateTime,
                startDateTime.plusDays(12L), // 12 days later
            )
        ).isEqualTo(1036800)
        assertThat(computeDurations.convertFromSeconds(1036800, DurationUnit.DAYS)).isEqualTo(12.0)
    }

    @Test
    fun `computeDurations should return correct values for all units`() {
        val durationInSeconds = 7200 // 2h
        assertThat(computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.NANOSECONDS)).isEqualTo(7.2E12)
        assertThat(computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.MICROSECONDS)).isEqualTo(7.2E9)
        assertThat(computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.MILLISECONDS)).isEqualTo(7.2E6)
        assertThat(computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.SECONDS)).isEqualTo(7.2E3)
        assertThat(computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.MINUTES)).isEqualTo(120.0)
        assertThat(computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)).isEqualTo(2.0)
        assertThat(computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.DAYS)).isEqualTo(0.08)
    }

}
