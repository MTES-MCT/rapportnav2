package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerOrganization
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
class MissionPassengerEntityTest {

    private fun createPassenger(
        startDate: LocalDate = LocalDate.of(2024, 1, 15),
        endDate: LocalDate = LocalDate.of(2024, 1, 20)
    ) = MissionPassengerEntity(
        id = 1,
        missionId = 100,
        fullName = "Test Passenger",
        organization = MissionPassengerOrganization.OTHER,
        startDate = startDate,
        endDate = endDate
    )

    @Test
    fun `isWithinMissionDates returns true when mission start is null`() {
        val passenger = createPassenger()
        assertThat(passenger.isWithinMissionDates(null, null)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns true when passenger dates are within mission dates`() {
        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 20)
        )
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")

        assertThat(passenger.isWithinMissionDates(missionStart, missionEnd)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns true when passenger dates exactly match mission dates`() {
        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 10),
            endDate = LocalDate.of(2024, 1, 25)
        )
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")

        assertThat(passenger.isWithinMissionDates(missionStart, missionEnd)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns false when passenger start date is before mission start`() {
        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 5),
            endDate = LocalDate.of(2024, 1, 15)
        )
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")

        assertThat(passenger.isWithinMissionDates(missionStart, missionEnd)).isFalse()
    }

    @Test
    fun `isWithinMissionDates returns false when passenger end date is after mission end`() {
        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 1, 30)
        )
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")

        assertThat(passenger.isWithinMissionDates(missionStart, missionEnd)).isFalse()
    }

    @Test
    fun `isWithinMissionDates returns false when passenger start date is after mission end`() {
        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 26),
            endDate = LocalDate.of(2024, 1, 28)
        )
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")
        val missionEnd = Instant.parse("2024-01-25T23:59:59Z")

        assertThat(passenger.isWithinMissionDates(missionStart, missionEnd)).isFalse()
    }

    @Test
    fun `isWithinMissionDates returns true when mission has no end date and passenger is after mission start`() {
        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 15),
            endDate = LocalDate.of(2024, 2, 15)
        )
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")

        assertThat(passenger.isWithinMissionDates(missionStart, null)).isTrue()
    }

    @Test
    fun `isWithinMissionDates returns false when mission has no end date but passenger is before mission start`() {
        val passenger = createPassenger(
            startDate = LocalDate.of(2024, 1, 5),
            endDate = LocalDate.of(2024, 1, 15)
        )
        val missionStart = Instant.parse("2024-01-10T00:00:00Z")

        assertThat(passenger.isWithinMissionDates(missionStart, null)).isFalse()
    }
}
