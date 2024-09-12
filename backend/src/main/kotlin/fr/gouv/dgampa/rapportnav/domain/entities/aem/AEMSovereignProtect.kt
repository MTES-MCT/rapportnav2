package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils
import java.time.ZonedDateTime

data class AEMSovereignProtect(
    val nbrOfHourAtSea: Int? = 0, // 7.1
    var nbrOfRecognizedVessel: Int? = 0, // 7.3
    val nbrOfControlledVessel: Int? = 0, // 7.4
) {
    constructor(
        navActions: List<NavActionEntity>,
        envActions: List<ExtendedEnvActionEntity?>,
        fishActions: List<ExtendedFishActionEntity?>,
        missionEndDateTime: ZonedDateTime?
    ) : this(
        nbrOfHourAtSea = getNbrHourAtSea(navActions, missionEndDateTime),
        nbrOfRecognizedVessel = getNbOfRecognizedVessel(navActions),
        nbrOfControlledVessel = getNbrOfControlledVessel(navActions, envActions, fishActions)
    ) {
    }

    companion object {
        fun getNbrHourAtSea(
            navActions: List<NavActionEntity>,
            missionEndDateTime: ZonedDateTime?
        ): Int {
            val sortedStatusActions =
                navActions.filter { it.actionType == ActionType.STATUS }
                    .filter { it.statusAction != null }
                    .map { action -> action.statusAction }
                    .sortedBy { it?.startDateTimeUtc }

            sortedStatusActions.windowed(2)
                .forEach { (first, second) -> first?.endDateTimeUtc = second?.startDateTimeUtc }

            if(sortedStatusActions.isNotEmpty()) sortedStatusActions.last()?.endDateTimeUtc = missionEndDateTime;
            val statusActions =
                anchoredActionEntities(sortedStatusActions) + navigationActionEntities(sortedStatusActions);
            return AEMUtils.getDurationInHours(statusActions).toInt();
        }

        fun getNbOfRecognizedVessel(
            navActions: List<NavActionEntity>
        ): Int {
            return 0;
        }

        fun getNbrOfControlledVessel(
            navActions: List<NavActionEntity>,
            envActions: List<ExtendedEnvActionEntity?>,
            fishActions: List<ExtendedFishActionEntity?>
        ): Int {
            return 0.plus(fishActions.size).plus(navActions.filter { it.controlAction != null }.size)
                .plus(envActions.filter { it?.controlAction?.action?.vehicleType == VehicleTypeEnum.VESSEL }.size);
        }

        private fun navigationActionEntities(navActions: List<ActionStatusEntity?>): List<ActionStatusEntity?> {
            return navActions.filter { it?.status == ActionStatusType.NAVIGATING }
        }

        private fun anchoredActionEntities(navActions: List<ActionStatusEntity?>): List<ActionStatusEntity?> {
            return navActions.filter { it?.status == ActionStatusType.ANCHORED };
        }
    }
}
