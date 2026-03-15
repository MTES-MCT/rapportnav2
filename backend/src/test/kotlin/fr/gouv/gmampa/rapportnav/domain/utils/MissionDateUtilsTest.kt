package fr.gouv.gmampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.utils.MissionDateUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate

class MissionDateUtilsTest {

    @Nested
    inner class InstantDatesValidation {

        @Test
        fun `returns true when mission start is null`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = Instant.parse("2024-01-15T10:00:00Z"),
                endDate = Instant.parse("2024-01-20T10:00:00Z"),
                missionStart = null,
                missionEnd = null
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns true when start date is null`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = null as Instant?,
                endDate = null,
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns true when dates are within mission range`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = Instant.parse("2024-01-15T10:00:00Z"),
                endDate = Instant.parse("2024-01-20T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns false when start date is before mission start`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = Instant.parse("2024-01-05T10:00:00Z"),
                endDate = Instant.parse("2024-01-15T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns false when start date is after mission end`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = Instant.parse("2024-01-26T10:00:00Z"),
                endDate = Instant.parse("2024-01-28T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns false when end date is after mission end`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = Instant.parse("2024-01-15T10:00:00Z"),
                endDate = Instant.parse("2024-01-30T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns true when mission has no end date and dates are after start`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = Instant.parse("2024-01-15T10:00:00Z"),
                endDate = Instant.parse("2024-02-15T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = null
            )
            assertThat(result).isTrue()
        }
    }

    @Nested
    inner class SingleDateValidation {

        @Test
        fun `returns true when date is null`() {
            val result = MissionDateUtils.isDateWithinMissionRange(
                date = null,
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns true when mission start is null`() {
            val result = MissionDateUtils.isDateWithinMissionRange(
                date = Instant.parse("2024-01-15T10:00:00Z"),
                missionStart = null,
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns true when date is within mission range`() {
            val result = MissionDateUtils.isDateWithinMissionRange(
                date = Instant.parse("2024-01-15T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns false when date is before mission start`() {
            val result = MissionDateUtils.isDateWithinMissionRange(
                date = Instant.parse("2024-01-05T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns false when date is after mission end`() {
            val result = MissionDateUtils.isDateWithinMissionRange(
                date = Instant.parse("2024-01-26T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns true when mission has no end date and date is after start`() {
            val result = MissionDateUtils.isDateWithinMissionRange(
                date = Instant.parse("2024-02-15T10:00:00Z"),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = null
            )
            assertThat(result).isTrue()
        }
    }

    @Nested
    inner class LocalDateValidation {

        @Test
        fun `returns true when mission start is null`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = LocalDate.of(2024, 1, 15),
                endDate = LocalDate.of(2024, 1, 20),
                missionStart = null,
                missionEnd = null
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns true when start date is null`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = null as LocalDate?,
                endDate = null,
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns true when dates are within mission range`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = LocalDate.of(2024, 1, 15),
                endDate = LocalDate.of(2024, 1, 20),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `returns false when start date is before mission start`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = LocalDate.of(2024, 1, 5),
                endDate = LocalDate.of(2024, 1, 15),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns false when start date is after mission end`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = LocalDate.of(2024, 1, 26),
                endDate = LocalDate.of(2024, 1, 28),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns false when end date is after mission end`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = LocalDate.of(2024, 1, 15),
                endDate = LocalDate.of(2024, 1, 30),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isFalse()
        }

        @Test
        fun `returns true when mission has no end date and dates are after start`() {
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = LocalDate.of(2024, 1, 15),
                endDate = LocalDate.of(2024, 2, 15),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = null
            )
            assertThat(result).isTrue()
        }

        @Test
        fun `correctly converts instant to local date for comparison`() {
            // Mission ends at end of Jan 25 UTC, so Jan 25 LocalDate should be valid
            val result = MissionDateUtils.isWithinMissionDates(
                startDate = LocalDate.of(2024, 1, 10),
                endDate = LocalDate.of(2024, 1, 25),
                missionStart = Instant.parse("2024-01-10T00:00:00Z"),
                missionEnd = Instant.parse("2024-01-25T23:59:59Z")
            )
            assertThat(result).isTrue()
        }
    }
}
