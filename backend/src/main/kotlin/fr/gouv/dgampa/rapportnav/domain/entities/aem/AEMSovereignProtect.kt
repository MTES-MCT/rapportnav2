package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils
import java.time.Instant

data class AEMSovereignProtect(
    val nbrOfHourAtSea: Double? = 0.0, // 7.1
    var nbrOfRecognizedVessel: Double? = 0.0, // 7.3
    val nbrOfControlledVessel: Double? = 0.0, // 7.4
) {
    constructor(
        navActions: List<MissionNavActionEntity>,
        envActions: List<MissionEnvActionEntity?>,
        fishActions: List<MissionFishActionEntity?>,
        missionEndDateTime: Instant?
    ) : this(
        nbrOfHourAtSea = getNbrHourAtSea(navActions, missionEndDateTime),
        nbrOfRecognizedVessel = getNbOfRecognizedVessel(navActions),
        nbrOfControlledVessel = getNbrOfControlledVessel(navActions, envActions, fishActions)
    )

    companion object {
        fun getNbrHourAtSea(
            navActions: List<MissionNavActionEntity>,
            missionEndDateTime: Instant?
        ): Double {
            val sortedStatusActions =
                navActions.filter { it.actionType == ActionType.STATUS }
                    .sortedBy { it.startDateTimeUtc }

            sortedStatusActions.windowed(2)
                .forEach { (first, second) -> first.endDateTimeUtc = second.startDateTimeUtc }

            if (sortedStatusActions.isNotEmpty()) sortedStatusActions.last().endDateTimeUtc = missionEndDateTime
            val statusActions = getAnchoredActions(sortedStatusActions) + getNavigationActions(sortedStatusActions)
            return AEMUtils.getDurationInHours2(statusActions)
        }

        fun getNbOfRecognizedVessel(navActions: List<MissionNavActionEntity>): Double {
            return 0.0
        }

        fun getNbrOfControlledVessel(
            navActions: List<MissionNavActionEntity>,
            envActions: List<MissionEnvActionEntity?>,
            fishActions: List<MissionFishActionEntity?>
        ): Double {
            val fishControls = fishActions.filter { it?.fishActionType == MissionActionType.SEA_CONTROL }.size
            val navControls = navActions.filter { it.actionType == ActionType.CONTROL }.size
            val envControls = envActions.filter { it?.vehicleType == VehicleTypeEnum.VESSEL }.sumOf { it?.actionNumberOfControls ?: 0 }
            return 0.0.plus(fishControls).plus(navControls).plus(envControls)
        }

        private fun getNavigationActions(navActions: List<MissionNavActionEntity?>): List<MissionNavActionEntity?> {
            return navActions.filter { it?.status == ActionStatusType.NAVIGATING }
        }

        private fun getAnchoredActions(navActions: List<MissionNavActionEntity?>): List<MissionNavActionEntity?> {
            return navActions.filter { it?.status == ActionStatusType.ANCHORED }
        }
    }
}
