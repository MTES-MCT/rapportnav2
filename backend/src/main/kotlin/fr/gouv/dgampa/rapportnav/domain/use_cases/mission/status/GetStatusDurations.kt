package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import java.time.ZonedDateTime
import kotlin.time.DurationUnit


@UseCase
class GetStatusDurations(
    private val computeDurations: ComputeDurations
) {

    data class ActionStatusWithDuration(
        val status: ActionStatusType,
        val duration: Long,
        val reason: ActionStatusReason? = null,
        val date: ZonedDateTime? = null
    )

    fun computeActionDurationsPerAction(
        missionStartDateTime: ZonedDateTime,
        missionEndDateTime: ZonedDateTime? = null,
        actions: List<ActionStatusEntity>? = listOf(),
        durationUnit: DurationUnit = DurationUnit.SECONDS
    ): MutableList<ActionStatusWithDuration> {
        val durations = mutableListOf<ActionStatusWithDuration>()


        var previousActionTime = missionStartDateTime
        if (!actions.isNullOrEmpty()) {
            for ((index, action) in actions.withIndex()) {
                val startTime = if (index == 0) missionStartDateTime else previousActionTime
                val durationInSeconds = if (missionEndDateTime != null || index < actions.size - 1) {
                    // If missionEndDateTime is provided or it's not the last action, calculate duration normally
                    val endTime =
                        if (index == actions.size - 1) missionEndDateTime else actions[index + 1].startDateTimeUtc
                    computeDurations.durationInSeconds(startTime, endTime)
                } else {
                    // If missionEndDateTime is null and it's the last action, exclude this duration
                    0
                }

                // Convert duration to the selected unit
                val duration = computeDurations.convertFromSeconds(durationInSeconds, durationUnit)

                durations.add(
                    ActionStatusWithDuration(
                        status = action.status,
                        duration = duration.toLong(),
                        reason = action.reason,
                        date = action.startDateTimeUtc
                    )
                )

                previousActionTime = action.startDateTimeUtc
            }
        }
        return durations
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
     *     ActionStatusWithDuration(status=ANCHORED, value=0, reason=null),
     *     ActionStatusWithDuration(status=DOCKED, value=0, reason=MAINTENANCE),
     *     ActionStatusWithDuration(status=DOCKED, value=0, reason=WEATHER),
     *     ActionStatusWithDuration(status=DOCKED, value=0, reason=REPRESENTATION),
     *     ActionStatusWithDuration(status=DOCKED, value=0, reason=ADMINISTRATION),
     *     ActionStatusWithDuration(status=DOCKED, value=0, reason=HARBOUR_CONTROL),
     *     ActionStatusWithDuration(status=DOCKED, value=0, reason=OTHER),
     *     ActionStatusWithDuration(status=NAVIGATING, value=0, reason=null),
     *     ActionStatusWithDuration(status=UNAVAILABLE, value=0, reason=TECHNICAL),
     *     ActionStatusWithDuration(status=UNAVAILABLE, value=0, reason=PERSONNEL),
     *     ActionStatusWithDuration(status=UNAVAILABLE, value=0, reason=OTHER)
     * ]
     * ```
     *
     * @param missionStartDateTime The start time of the mission.
     * @param missionEndDateTime The end time of the mission (optional). If provided, durations are calculated
     *     until this time; otherwise, durations are calculated until the current time for all but the last action.
     * @param actions The list of action status entities (optional). If not provided, default durations with zero
     *     values for all possible status and reason combinations are returned.
     * @param durationUnit which duration unit (seconds, minutes, hours)
     * @return A list of ActionStatusWithDuration objects representing the computed durations for each action status.
     */
    fun computeActionDurationsForAllMission(
        missionStartDateTime: ZonedDateTime,
        missionEndDateTime: ZonedDateTime? = null,
        actions: List<ActionStatusEntity>? = listOf(),
        durationUnit: DurationUnit = DurationUnit.SECONDS
    ): List<ActionStatusWithDuration> {
        val durations = this.computeActionDurationsPerAction(
            missionStartDateTime = missionStartDateTime,
            missionEndDateTime = missionEndDateTime,
            actions = actions,
            durationUnit = durationUnit
        )

        // Add default values for all combinations of status and reason
        for (status in ActionStatusType.values()) {
            if (status != ActionStatusType.UNKNOWN) {
                val reasons = getSelectOptionsForType(status) ?: listOf(null)
                for (reason in reasons) {
                    if (durations.none { it.status == status && it.reason == reason }) {
                        durations.add(ActionStatusWithDuration(status = status, duration = 0, reason = reason))
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
