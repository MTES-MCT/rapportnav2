package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

data class AEMSovereignProtect(
    val nbrOfHourAtSea: Int? = 0, // 7.1
    var nbrOfRecognizedVessel: Int? = 0, // 7.3
    val nbrOfControlledVessel: Int? = 0, // 7.4
) {
    constructor(
        navActions: List<NavActionEntity>,
        envActions: List<ExtendedEnvActionEntity?>,
        fishActions: List<ExtendedFishActionEntity?>
    ) : this(
        nbrOfHourAtSea = getNbrHourAtSea(navActions),
        nbrOfRecognizedVessel = getNbOfRecognizedVessel(navActions),
        nbrOfControlledVessel = getNbrOfControlledVessel(navActions, envActions, fishActions)
    ) {
    }

    companion object {
        fun getNbrHourAtSea(
            navActions: List<NavActionEntity>
        ): Int {
            val statusActions = anchoredActionEntities(navActions) + navigationActionEntities(navActions);
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

        private fun navigationActionEntities(navActions: List<NavActionEntity>): List<ActionStatusEntity?> {
            return navActions.filter { it.statusAction != null }.map { action -> action.statusAction }
                .filter { it?.status == ActionStatusType.NAVIGATING }
        }

        private fun anchoredActionEntities(navActions: List<NavActionEntity>): List<ActionStatusEntity?> {
            return navActions.filter { it.statusAction != null }.map { action -> action.statusAction }
                .filter { it?.status == ActionStatusType.ANCHORED };
        }
    }
}
