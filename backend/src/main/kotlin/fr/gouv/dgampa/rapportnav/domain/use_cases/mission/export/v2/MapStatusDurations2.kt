package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations2
import java.time.Instant
import kotlin.math.ceil
import kotlin.time.DurationUnit

@UseCase
class MapStatusDurations2(
    private val getStatusDurations: GetStatusDurations2,
) {

    private inline fun List<GetStatusDurations2.ActionStatusWithDuration>.findDuration(predicate: (GetStatusDurations2.ActionStatusWithDuration) -> Boolean): Int {
        return ceil(find(predicate)?.duration ?: 0.0).toInt()
    }

    fun execute(
        statuses: List<MissionNavActionEntity>,
        durationUnit: DurationUnit = DurationUnit.SECONDS,
        endDateTimeUtc: Instant? = null,
    ): Map<String, Map<String, Int>> {
        val durations = getStatusDurations.computeActionDurationsForAllMission(
            missionEndDateTime = endDateTimeUtc,
            actions = statuses,
            durationUnit = durationUnit
        )

        val atSeaDurations = mapOf(
            "navigationEffective" to durations.findDuration { it.status == ActionStatusType.NAVIGATING },
            "mouillage" to durations.findDuration { it.status == ActionStatusType.ANCHORED },
            "total" to 0
        ).toMutableMap()
        atSeaDurations["total"] = atSeaDurations.values.sum()

        val dockingDurations = mapOf(
            "maintenance" to durations.findDuration { it.reason == ActionStatusReason.MAINTENANCE },
            "meteo" to durations.findDuration { it.reason == ActionStatusReason.WEATHER },
            "representation" to durations.findDuration { it.reason == ActionStatusReason.REPRESENTATION },
            "adminFormation" to durations.findDuration { it.reason == ActionStatusReason.ADMINISTRATION },
            "autre" to durations.findDuration { it.reason == ActionStatusReason.OTHER },
            "contrPol" to durations.findDuration { it.reason == ActionStatusReason.HARBOUR_CONTROL },
            "total" to 0
        ).toMutableMap()
        dockingDurations["total"] = dockingDurations.values.sum()

        val unavailabilityDurations = mapOf(
            "technique" to durations.findDuration { it.reason == ActionStatusReason.TECHNICAL },
            "personnel" to durations.findDuration { it.reason == ActionStatusReason.PERSONNEL },
            "total" to 0
        ).toMutableMap()
        unavailabilityDurations["total"] = unavailabilityDurations.values.sum()

        return mapOf(
            "atSeaDurations" to atSeaDurations.toMap(),
            "dockingDurations" to dockingDurations.toMap(),
            "unavailabilityDurations" to unavailabilityDurations.toMap()
        )
    }
}
