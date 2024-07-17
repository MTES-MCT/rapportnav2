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
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.time.DurationUnit


@SpringBootTest(classes = [GetStatusDurations::class, ComputeDurations::class])

class GetStatusDurationsTests {

    @Autowired
    private lateinit var getStatusDurations: GetStatusDurations

    private lateinit var defaultReturnValues: List<GetStatusDurations.ActionStatusWithDuration>

    private val missionStartDateTime = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))
    private val missionEndDateTime = ZonedDateTime.of(2023, 6, 30, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))

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
                startDateTimeUtc = missionStartDateTime.plusHours(2),
                status = ActionStatusType.ANCHORED
            ),
            ActionStatusEntity(
                UUID.randomUUID(),
                missionId = 1,
                startDateTimeUtc = missionStartDateTime.plusHours(5),
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.WEATHER
            ),
            ActionStatusEntity(
                UUID.randomUUID(),
                missionId = 1,
                startDateTimeUtc = missionStartDateTime.plusHours(7),
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
            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 10.0, null, ZonedDateTime.now()),
            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 5.0, null, ZonedDateTime.now()),
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.DOCKED,
                8.0,
                ActionStatusReason.TECHNICAL,
                ZonedDateTime.now()
            ),
            GetStatusDurations.ActionStatusWithDuration(
                ActionStatusType.DOCKED,
                3.0,
                ActionStatusReason.WEATHER,
                ZonedDateTime.now()
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
