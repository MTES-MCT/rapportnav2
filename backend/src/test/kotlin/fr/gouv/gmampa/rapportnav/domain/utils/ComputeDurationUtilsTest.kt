package fr.gouv.gmampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZonedDateTime
import kotlin.time.DurationUnit

@SpringBootTest(classes = [ComputeDurationUtils::class])
class ComputeDurationUtilsTest {

    @Test
    fun `Should convert from seconds to duration regarding duration unit`() {
        //convertFromSeconds
        val duration  = 3600494;
        assertThat(ComputeDurationUtils.convertFromSeconds(duration, DurationUnit.DAYS)).isEqualTo(41.67);
        assertThat(ComputeDurationUtils.convertFromSeconds(duration, DurationUnit.HOURS)).isEqualTo(1000.14);
        assertThat(ComputeDurationUtils.convertFromSeconds(duration, DurationUnit.MINUTES)).isEqualTo(60008.23);
        assertThat(ComputeDurationUtils.convertFromSeconds(duration, DurationUnit.SECONDS)).isEqualTo(3600494.0);
        assertThat(ComputeDurationUtils.convertFromSeconds(duration, DurationUnit.MILLISECONDS)).isEqualTo(3.600494E9);
        assertThat(ComputeDurationUtils.convertFromSeconds(duration, DurationUnit.NANOSECONDS)).isEqualTo(3.600494E15);
        assertThat(ComputeDurationUtils.convertFromSeconds(duration, DurationUnit.MICROSECONDS)).isEqualTo(3.600494E12);
    }

    @Test
    fun `Should compute duration in seconds from 2 dates`() {
        val duration = 3600494;
        val startDateUtc = ZonedDateTime.parse("2024-01-09T10:00:00Z");
        val endDateUtc = ZonedDateTime.parse("2024-01-11T14:30:00Z");
        assertThat(ComputeDurationUtils.durationInSeconds(startDateUtc, endDateUtc)).isEqualTo(duration);
    }

    @Test
    fun `Should compute duration in hours from 2 dates`() {
        val duration = 52.5;
        val startDateUtc = ZonedDateTime.parse("2024-01-09T10:00:00Z");
        val endDateUtc = ZonedDateTime.parse("2024-01-11T14:30:00Z");
        assertThat(ComputeDurationUtils.durationInHours(startDateUtc, endDateUtc)).isEqualTo(duration);
    }
}
