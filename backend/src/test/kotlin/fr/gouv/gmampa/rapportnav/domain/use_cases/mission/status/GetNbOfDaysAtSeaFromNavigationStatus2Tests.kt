package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations2
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.time.temporal.ChronoUnit


@SpringBootTest(classes = [GetNbOfDaysAtSeaFromNavigationStatus2::class, GetStatusDurations2::class, ComputeDurations::class])
class GetNbOfDaysAtSeaFromNavigationStatus2Tests {

    @Autowired
    private lateinit var getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus2

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
    fun `execute should handle empty list of statuses`() {
        val statuses = emptyList<MissionNavActionEntity>()

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        )

        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should return 0 when no Navigation or Anchored statuses`() {
        val statuses = listOf(
            MissionNavActionEntityMock.create(actionType = ActionType.STATUS, status = ActionStatusType.DOCKED, startDateTimeUtc = missionStartDateTime),
            MissionNavActionEntityMock.create(actionType = ActionType.STATUS, status = ActionStatusType.UNAVAILABLE, startDateTimeUtc = missionStartDateTime.plus(1, ChronoUnit.HOURS)),
            MissionNavActionEntityMock.create(actionType = ActionType.STATUS, status = ActionStatusType.DOCKED, startDateTimeUtc = missionStartDateTime.plus(2, ChronoUnit.HOURS)),
        )
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
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = missionStartDateTime.plus(1, ChronoUnit.HOURS)
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = missionStartDateTime.plus(2, ChronoUnit.HOURS)
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.UNAVAILABLE,
                startDateTimeUtc = missionStartDateTime.plus(3, ChronoUnit.HOURS)
            ),
            // second day has 7
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime.plus(25, ChronoUnit.HOURS)
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = missionStartDateTime.plus(30, ChronoUnit.HOURS)
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.UNAVAILABLE,
                startDateTimeUtc = missionStartDateTime.plus(32, ChronoUnit.HOURS)
            ),
            // the rest of the days doesn't count
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime = missionStartDateTime,
            missionEndDateTime = missionEndDateTime,
            actions = statuses
        )
        assertThat(value).isEqualTo(1)
    }

    @Test
    fun `execute should return the amount of days exceeding fours hours of navigation even when a status spans over several days`() {
        val startDateTime = Instant.parse("2024-03-11T06:00:00Z")
        val endDateTime = Instant.parse("2024-03-22T17:00:00Z")
        val statuses = listOf(
            // day 1
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-11T03:00:00Z")
            ),
            // day 2
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-12T05:48:00Z")
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-12T15:54:00Z")
            ),
            // day 3
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-13T06:12:00Z")
            ),
            // day 4
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-14T13:21:00Z")
            ),
            // day 5
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-15T18:54:00Z")
            ),
            // skip to day 10
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = Instant.parse("2024-03-20T17:00:00Z")
            ),
            // day 11
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2024-03-21T06:54:00Z")
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-21T12:42:00Z")
            ),
            // day 12
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = Instant.parse("2024-03-22T15:00:00Z")
            ),
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime = startDateTime,
            missionEndDateTime = endDateTime,
            actions = statuses
        )

        assertThat(value).isEqualTo(10)
    }

    @Test
    fun `execute should return 0 when missionEndDateTime is before missionStartDateTime`() {
        val statuses = listOf(
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime
            )
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime = missionEndDateTime,
            missionEndDateTime = missionStartDateTime,
            actions = statuses
        )

        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should ignore actions with actionType different from STATUS`() {
        val statuses = listOf(
            MissionNavActionEntityMock.create(
                actionType = ActionType.NOTE, // ignored
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = missionStartDateTime.plus(1, ChronoUnit.HOURS)
            )
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        )
        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should handle single status covering entire mission`() {
        val statuses = listOf(
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime
            )
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        )

        // this spans 12 days but only counts if >4h per day -> all should count
        assertThat(value).isEqualTo(12)
    }
}
