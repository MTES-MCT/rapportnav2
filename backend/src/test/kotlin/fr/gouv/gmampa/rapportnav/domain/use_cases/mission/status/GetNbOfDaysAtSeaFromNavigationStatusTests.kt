package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionStatusMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.time.temporal.ChronoUnit


@SpringBootTest(classes = [GetNbOfDaysAtSeaFromNavigationStatus::class, GetStatusDurations::class, ComputeDurations::class])

class GetNbOfDaysAtSeaFromNavigationStatusTests {

    @Autowired
    private lateinit var getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus

    private val missionStartDateTime = Instant.parse("2022-01-01T11:00:00Z")
    private val missionEndDateTime = Instant.parse("2022-01-12T21:00:00Z")


    @Test
    fun `execute should return 0 when no statuses at all`() {
        val statuses = null
        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should return 0 when no Navigation or Anchored statuses`() {
        val statuses = listOf(NavActionStatusMock.create(status = ActionStatusType.DOCKED))
        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should return the amount of days exceeding fours hours of navigation`() {
        val statuses = listOf(
            // first day has only 2 hours of navigation
            NavActionStatusMock.create(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime
            ),
            NavActionStatusMock.create(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = missionStartDateTime.plus(1, ChronoUnit.HOURS)
            ),
            NavActionStatusMock.create(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = missionStartDateTime.plus(2, ChronoUnit.HOURS)
            ),
            NavActionStatusMock.create(
                status = ActionStatusType.UNAVAILABLE,
                startDateTimeUtc = missionStartDateTime.plus(3, ChronoUnit.HOURS)
            ),
            // second day has 7
            NavActionStatusMock.create(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime.plus(25, ChronoUnit.HOURS)
            ),
            NavActionStatusMock.create(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = missionStartDateTime.plus(30, ChronoUnit.HOURS)
            ),
            NavActionStatusMock.create(
                status = ActionStatusType.UNAVAILABLE,
                startDateTimeUtc = missionStartDateTime.plus(32, ChronoUnit.HOURS)
            ),
            // the rest of the days doesn't count
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(1)
    }

    @Test
    fun `execute should return the amount of days exceeding fours hours of navigation even when a status spans over several days`() {
        val startDateTime = Instant.parse("2024-03-11T06:00:00Z")
        val endDateTime = Instant.parse("2024-03-22T17:00:00Z")
        val statuses = listOf(
            // day 1
            NavActionStatusMock.create(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-11T03:00:00Z")
            ),
            // day 2
            NavActionStatusMock.create(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-12T05:48:00Z")
            ),
            NavActionStatusMock.create(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-12T15:54:00Z")
            ),
            // day 3
            NavActionStatusMock.create(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-13T06:12:00Z")
            ),
            // day 4
            NavActionStatusMock.create(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-14T13:21:00Z")
            ),
            // day 5
            NavActionStatusMock.create(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-15T18:54:00Z")
            ),
            // skip to day 10
            NavActionStatusMock.create(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = Instant.parse("2024-03-20T17:00:00Z")
            ),
            // day 11
            NavActionStatusMock.create(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-21T06:54:00Z")
            ),
            NavActionStatusMock.create(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-21T12:42:00Z")
            ),
            // day 12
            NavActionStatusMock.create(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-22T15:00:00Z")
            ),
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            startDateTime,
            endDateTime,
            statuses
        );
        assertThat(value).isEqualTo(10)
    }


}
