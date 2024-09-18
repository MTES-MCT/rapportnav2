package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.DurationUnit


@SpringBootTest(classes = [GetStatusDurations::class, ComputeDurations::class])

class GetStatusDurationsTests {

    @Autowired
    private lateinit var getStatusDurations: GetStatusDurations

    private lateinit var defaultReturnValues: List<GetStatusDurations.ActionStatusWithDuration>

    private val missionStartDateTime = Instant.parse("2023-06-19T00:00:00.000+01:00")
    private val missionEndDateTime = Instant.parse("2023-06-30T00:00:00.000+01:00")

    @BeforeEach
    fun setUp() {
        defaultReturnValues = ActionStatusType.entries.filter { it != ActionStatusType.UNKNOWN }.flatMap { status ->
            getStatusDurations.getSelectOptionsForType(status)?.map { reason ->
                GetStatusDurations.ActionStatusWithDuration(status, 0.0, reason)
            } ?: listOf(GetStatusDurations.ActionStatusWithDuration(status, 0.0))
        }
    }

    @Test
    fun `execute should return the default when statuses is null`() {
        val statuses = null
        val values = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        )
        assertThat(values).containsExactlyInAnyOrder(*defaultReturnValues.toTypedArray())
    }

    @Test
    fun `execute should return the default when statuses is empty`() {
        val statuses = listOf<ActionStatusEntity>()
        val values = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        )
        assertThat(values).containsExactlyInAnyOrder(*defaultReturnValues.toTypedArray())
    }

    @Test
    fun `execute should return duration for single status`() {
        val actionStatus = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            startDateTimeUtc = missionStartDateTime,
            status = ActionStatusType.NAVIGATING,
        )
        val statuses = listOf(actionStatus)

        val expectedDuration = Duration.between(actionStatus.startDateTimeUtc, missionEndDateTime).toSeconds()

        val values = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        )

        assertThat(values).contains(
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.NAVIGATING,
                duration = expectedDuration.toDouble(),
                reason = null,
            )
        )
    }

    @Test
    fun `execute should return durations excluding last action when missionEndDateTime is null`() {
        // Define the list of action status entities
        val actions = listOf(
            ActionStatusEntity(
                UUID.randomUUID(),
                missionId = 1,
                startDateTimeUtc = missionStartDateTime,
                status = ActionStatusType.NAVIGATING
            ),
            ActionStatusEntity(
                UUID.randomUUID(),
                missionId = 1,
                startDateTimeUtc = missionStartDateTime.plus(2, ChronoUnit.HOURS),
                status = ActionStatusType.ANCHORED
            ),
            ActionStatusEntity(
                UUID.randomUUID(),
                missionId = 1,
                startDateTimeUtc = missionStartDateTime.plus(5, ChronoUnit.HOURS),
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.WEATHER
            ),
            ActionStatusEntity(
                UUID.randomUUID(),
                missionId = 1,
                startDateTimeUtc = missionStartDateTime.plus(7, ChronoUnit.HOURS),
                status = ActionStatusType.UNAVAILABLE,
                reason = ActionStatusReason.TECHNICAL
            )
        )

        // values in hours
        val valuesInHours = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime,
            missionEndDateTime,
            actions,
            DurationUnit.HOURS
        )
        val expectedValuesInHours = listOf(
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.NAVIGATING,
                2.0,
                null,
            ),
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.ANCHORED,
                3.0,
                null,
            ),
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.DOCKED,
                2.0,
                ActionStatusReason.WEATHER,
            ),
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.UNAVAILABLE,
                257.0, // all the rest of the mission
                ActionStatusReason.TECHNICAL,
            )
        )
        assertThat(valuesInHours).containsAll(expectedValuesInHours)
    }


    @Test
    fun `sumDurationsByStatusAndReason should correctly sum durations by status and reason`() {
        val actions = listOf(
            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 10.0, null, Instant.now()),
            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 5.0, null, Instant.now()),
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.DOCKED,
                8.0,
                ActionStatusReason.TECHNICAL,
                Instant.now()
            ),
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.DOCKED,
                3.0,
                ActionStatusReason.WEATHER,
                Instant.now()
            )
        )

        val result = getStatusDurations.sumDurationsByStatusAndReason(actions)

        assertThat(result).hasSize(3)
        assertThat(result).containsExactlyInAnyOrder(
            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 15.0, null),
            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.DOCKED, 8.0, ActionStatusReason.TECHNICAL),
            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.DOCKED, 3.0, ActionStatusReason.WEATHER)
        )
    }

}
