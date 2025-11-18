package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations2
import java.time.Instant
import kotlin.math.ceil
import kotlin.time.DurationUnit

@UseCase
class MapStatusDurations2(
    private val getStatusDurations: GetStatusDurations2,
    private val getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus2,
) {

    private inline fun List<GetStatusDurations2.ActionStatusWithDuration>.findDuration(predicate: (GetStatusDurations2.ActionStatusWithDuration) -> Boolean): Double {
        return ceil(find(predicate)?.duration ?: 0.0)
    }

    fun execute(
        statuses: List<MissionNavActionEntity>,
        controls: List<MissionActionEntity>? = listOf(),
        startDateTimeUtc: Instant? = null,
        endDateTimeUtc: Instant? = null,
    ): Map<String, Map<String, Double>> {
        val durations = getStatusDurations.computeActionDurationsForAllMission(
            missionEndDateTime = endDateTimeUtc,
            actions = statuses,
            durationUnit = DurationUnit.HOURS
        )

        val nbOfDaysAtSea = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime = startDateTimeUtc!!,
            missionEndDateTime = endDateTimeUtc,
            actions = statuses,
            durationUnit = DurationUnit.HOURS
        )

        val atSeaControls = controls
            .orEmpty()
            .filter { it.status === ActionStatusType.NAVIGATING || it.status === ActionStatusType.ANCHORED }
        val atSeaDurations = mapOf(
            "navigationDurationInHours" to durations.findDuration { it.status == ActionStatusType.NAVIGATING },
            "anchoredDurationInHours" to durations.findDuration { it.status == ActionStatusType.ANCHORED },
            "totalDurationInHours" to 0.0,
            "nbControls" to 0.0,
            "nbOfDaysAtSea" to 0.0,
        ).toMutableMap()
        atSeaDurations["totalDurationInHours"] = atSeaDurations.values.sum()
        atSeaDurations["nbControls"] = atSeaControls.size.toDouble()
        atSeaDurations["nbOfDaysAtSea"] = nbOfDaysAtSea.toDouble()

        val dockingControls = controls.orEmpty().filter { it.status === ActionStatusType.DOCKED }
        val dockingDurations = mapOf(
            "maintenanceDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.MAINTENANCE },
            "meteoDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.WEATHER },
            "representationDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.REPRESENTATION },
            "adminFormationDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.ADMINISTRATION },
            "mcoDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.MCO_AND_LOGISTICS },
            "otherDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.OTHER },
            "contrPolDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.HARBOUR_CONTROL },
            "totalDurationInHours" to 0.0,
            "nbControls" to 0.0,
        ).toMutableMap()
        dockingDurations["totalDurationInHours"] = dockingDurations.values.sum()
        dockingDurations["nbControls"] = dockingControls.size.toDouble()

        val unavailabilityControls = controls.orEmpty().filter { it.status === ActionStatusType.UNAVAILABLE }
        val unavailabilityDurations = mapOf(
            "technicalDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.TECHNICAL },
            "personnelDurationInHours" to durations.findDuration { it.reason == ActionStatusReason.PERSONNEL },
            "totalDurationInHours" to 0.0,
            "nbControls" to 0.0,
        ).toMutableMap()
        unavailabilityDurations["totalDurationInHours"] = unavailabilityDurations.values.sum()
        unavailabilityDurations["nbControls"] = unavailabilityControls.size.toDouble()

        return mapOf(
            "atSea" to atSeaDurations.toMap(),
            "docked" to dockingDurations.toMap(),
            "unavailable" to unavailabilityDurations.toMap()
        )
    }
}
