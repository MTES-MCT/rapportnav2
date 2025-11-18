package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations2
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import kotlin.time.DurationUnit


@SpringBootTest(classes = [GetStatusDurations2::class, ComputeDurations::class])
class GetStatusDurations2Tests {

    @Autowired
    private lateinit var getStatusDurations2: GetStatusDurations2

    private fun action(
        status: ActionStatusType,
        date: Instant,
        type: ActionType = ActionType.STATUS
    ) = MissionNavActionEntityMock.create(
        status = status,
        actionType = type,
        startDateTimeUtc = date
    )

    // ---------------------------
    // computeDurationsByAction()
    // ---------------------------

    @Test
    fun `computeDurationsByAction returns empty list when no actions`() {
        val actions = null
        val result = getStatusDurations2.computeDurationsByAction(actions = actions)

        assertThat(result).isEmpty()
    }
    @Test
    fun `computeDurationsByAction returns empty list when no status-type actions`() {
        val actions = listOf(
            action(ActionStatusType.DOCKED, Instant.now(), type = ActionType.NOTE)
        )
        val result = getStatusDurations2.computeDurationsByAction(actions = actions)

        assertThat(result).isEmpty()
    }

    @Test
    fun `computeDurationsByAction returns duration until next action`() {
        val t1 = Instant.parse("2024-01-01T00:00:00Z")
        val t2 = Instant.parse("2024-01-01T01:00:00Z")
        val t3 = Instant.parse("2024-01-01T02:30:00Z")

        val actions = listOf(
            action(ActionStatusType.NAVIGATING, t1),
            action(ActionStatusType.DOCKED, t2),
            action(ActionStatusType.UNAVAILABLE, t3)
        )

        val result = getStatusDurations2.computeDurationsByAction(actions = actions, durationUnit = DurationUnit.HOURS)

        assertThat(result).hasSize(3)
        assertThat(result[0].duration).isEqualTo(1.0)     // 1 hour
        assertThat(result[1].duration).isEqualTo(1.5)     // 1h 30m
    }

    @Test
    fun `computeDurationsByAction last action gets zero duration when missionEndDateTime is null`() {
        val t1 = Instant.parse("2024-01-01T00:00:00Z")
        val t2 = Instant.parse("2024-01-01T01:00:00Z")

        val actions = listOf(
            action(ActionStatusType.NAVIGATING, t1),
            action(ActionStatusType.DOCKED, t2)
        )

        val result = getStatusDurations2.computeDurationsByAction(actions = actions)

        assertThat(result[1].duration).isEqualTo(0.0)  // last action → 0
    }

    @Test
    fun `computeDurationsByAction uses missionEndDateTime for last action`() {
        val t1 = Instant.parse("2024-01-01T00:00:00Z")
        val end = Instant.parse("2024-01-01T02:00:00Z")

        val actions = listOf(action(ActionStatusType.NAVIGATING, t1))

        val result = getStatusDurations2.computeDurationsByAction(
            missionEndDateTime = end,
            actions = actions,
            durationUnit = DurationUnit.MINUTES
        )

        assertThat(result[0].duration).isEqualTo(120.0)
    }

    // ---------------------------
    // sumDurationsByStatusAndReason()
    // ---------------------------

    @Test
    fun `sumDurationsByStatusAndReason groups by status and reason`() {
        val data = listOf(
            GetStatusDurations2.ActionStatusWithDuration(1.0, ActionStatusType.NAVIGATING, null),
            GetStatusDurations2.ActionStatusWithDuration(2.0, ActionStatusType.NAVIGATING, null),
            GetStatusDurations2.ActionStatusWithDuration(3.0, ActionStatusType.DOCKED, ActionStatusReason.WEATHER)
        )

        val result = getStatusDurations2.sumDurationsByStatusAndReason(data)

        assertThat(result.find { it.status == ActionStatusType.NAVIGATING }!!.duration).isEqualTo(3.0)
        assertThat(result.find { it.status == ActionStatusType.DOCKED }!!.duration).isEqualTo(3.0)
    }

    // ---------------------------
    // computeActionDurationsForAllMission()
    // ---------------------------

    @Test
    fun `computeActionDurationsForAllMission returns defaults when actions is empty`() {
        val result = getStatusDurations2.computeActionDurationsForAllMission(actions = emptyList())

        // by default: every status except UNKNOWN, with all matching reasons
        val dockedReasons = getStatusDurations2.getSelectOptionsForType(ActionStatusType.DOCKED)!!
        val unavailableReasons = getStatusDurations2.getSelectOptionsForType(ActionStatusType.UNAVAILABLE)!!

        assertThat(result).anyMatch { it.status == ActionStatusType.ANCHORED }
        assertThat(result).anyMatch { it.reason == dockedReasons.first() }
        assertThat(result).anyMatch { it.reason == unavailableReasons.first() }
        assertThat(result.all { it.duration == 0.0 }).isTrue()
    }

    // ---------------------------
    // getSelectOptionsForType()
    // ---------------------------

    @Test
    fun `getSelectOptionsForType returns null for types without reasons`() {
        assertThat(getStatusDurations2.getSelectOptionsForType(ActionStatusType.NAVIGATING)).isNull()
        assertThat(getStatusDurations2.getSelectOptionsForType(ActionStatusType.ANCHORED)).isNull()
    }
}
