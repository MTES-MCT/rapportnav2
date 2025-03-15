package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import java.time.Instant
import kotlin.time.DurationUnit


@UseCase
class GetStatusDurations2(
    private val computeDurations: ComputeDurations
) {

    data class ActionStatusWithDuration(
        val duration: Double,
        val status: ActionStatusType? = null,
        val reason: ActionStatusReason? = null,
        val datetime: Instant? = null
    )

    fun computeDurationsByAction(
        missionEndDateTime: Instant? = null,
        actions: List<MissionNavActionEntity>? = listOf(),
        durationUnit: DurationUnit = DurationUnit.SECONDS
    ): MutableList<ActionStatusWithDuration> {
        // filter actions to keep Status actions only
        val statuses = actions?.filter { it.actionType == ActionType.STATUS }

        val durations = mutableListOf<ActionStatusWithDuration>()

        if (!statuses.isNullOrEmpty()) {
            for ((index, action) in statuses.withIndex()) {
                val startTime = action.startDateTimeUtc
                val durationInSeconds = if (missionEndDateTime != null || index < actions.size - 1) {
                    // If missionEndDateTime is provided or if it's not the last action, calculate duration normally
                    val endTime =
                        if (index == actions.size - 1) missionEndDateTime else actions[index + 1].startDateTimeUtc
                    computeDurations.durationInSeconds(startTime, endTime)
                } else {
                    // If missionEndDateTime is null, and it's the last action, exclude this duration
                    0
                }

                // Convert duration to the selected unit
                val duration = if (durationInSeconds != null) computeDurations.convertFromSeconds(
                    durationInSeconds,
                    durationUnit
                ) else 0.0

                durations.add(
                    ActionStatusWithDuration(
                        status = action.status,
                        duration = duration,
                        reason = action.reason,
                        datetime = action.startDateTimeUtc
                    )
                )

            }
        }
        return durations
    }

    fun sumDurationsByStatusAndReason(actions: List<ActionStatusWithDuration>): MutableList<ActionStatusWithDuration> {
        return actions.groupBy { it.status to it.reason }
            .map { (key, group) ->
                val (status, reason) = key
                val totalDuration = group.sumOf { it.duration }
                ActionStatusWithDuration(status = status, duration = totalDuration, reason = reason)
            }.toMutableList()
    }

    /**
     * Computes the durations of various action statuses along with their reasons, returning an exhaustive list.
     *
     * This function computes the durations for each action status, including different reasons, and returns a list
     * containing all possible combinations of status and reason with their corresponding durations. If a mission end
     * time is provided, the duration is calculated until that time; otherwise, the duration is calculated until the
     * current time for all but the last action. If no action statuses are provided, the function returns default
     * durations with zero values for all possible status and reason combinations.
     *
     * Example of default output:
     * ```
     * [
     *     ActionStatusWithDuration(status=ANCHORED, duration=0, reason=null, date=null),
     *     ActionStatusWithDuration(status=DOCKED, duration=0, reason=MAINTENANCE, date=null),
     *     ActionStatusWithDuration(status=DOCKED, duration=0, reason=WEATHER, date=null),
     *     ActionStatusWithDuration(status=DOCKED, duration=0, reason=REPRESENTATION, date=null),
     *     ActionStatusWithDuration(status=DOCKED, duration=0, reason=ADMINISTRATION, date=null),
     *     ActionStatusWithDuration(status=DOCKED, duration=0, reason=HARBOUR_CONTROL, date=null),
     *     ActionStatusWithDuration(status=DOCKED, duration=0, reason=OTHER, date=null),
     *     ActionStatusWithDuration(status=NAVIGATING, duration=0, reason=null, date=null),
     *     ActionStatusWithDuration(status=UNAVAILABLE, duration=0, reason=TECHNICAL, date=null),
     *     ActionStatusWithDuration(status=UNAVAILABLE, duration=0, reason=PERSONNEL, date=null),
     *     ActionStatusWithDuration(status=UNAVAILABLE, duration=0, reason=OTHER, date=null)
     * ]
     * ```
     *
     * @param missionEndDateTime The end time of the mission (optional). If provided, durations are calculated
     *     until this time; otherwise, durations are calculated until the current time for all but the last action.
     * @param actions The list of action status entities (optional). If not provided, default durations with zero
     *     values for all possible status and reason combinations are returned.
     * @param durationUnit which duration unit (seconds, minutes, hours)
     * @return A list of ActionStatusWithDuration objects representing the computed durations for each action status.
     */
    fun computeActionDurationsForAllMission(
        missionEndDateTime: Instant? = null,
        actions: List<MissionNavActionEntity>? = listOf(),
        durationUnit: DurationUnit = DurationUnit.SECONDS
    ): List<ActionStatusWithDuration> {
        val durationsPerStatusAction = this.computeDurationsByAction(
            missionEndDateTime = missionEndDateTime,
            actions = actions,
            durationUnit = durationUnit
        )

        val durations = this.sumDurationsByStatusAndReason(durationsPerStatusAction)

        // Add default values for all combinations of status and reason
        for (status in ActionStatusType.entries) {
            if (status != ActionStatusType.UNKNOWN) {
                val reasons = getSelectOptionsForType(status) ?: listOf(null)
                for (reason in reasons) {
                    if (durations.none { it.status == status && it.reason == reason }) {
                        durations.add(ActionStatusWithDuration(status = status, duration = 0.0, reason = reason))
                    }
                }
            }
        }

        return durations.sortedBy { it.status.toString() }
    }

    fun getSelectOptionsForType(type: ActionStatusType): List<ActionStatusReason>? {
        return when (type) {
            ActionStatusType.DOCKED -> listOf(
                ActionStatusReason.MAINTENANCE,
                ActionStatusReason.WEATHER,
                ActionStatusReason.REPRESENTATION,
                ActionStatusReason.ADMINISTRATION,
                ActionStatusReason.HARBOUR_CONTROL,
                ActionStatusReason.OTHER
            )

            ActionStatusType.UNAVAILABLE -> listOf(
                ActionStatusReason.TECHNICAL,
                ActionStatusReason.PERSONNEL,
                ActionStatusReason.OTHER
            )

            else -> null
        }
    }

}
