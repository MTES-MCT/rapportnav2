package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMVesselRescue(
    val nbrOfHourAtSea: Double? = 0.0, // 2.1
    val nbrOfRescuedOperation: Double? = 0.0,// 2.3
    val nbrOfNoticedVessel: Double? = 0.0, // 2.4
    val nbrOfTowedVessel: Double? = 0.0, //2.7
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfRescuedOperation = actionRescueEntities(navActions).size.toDouble(),
        nbrOfTowedVessel = getNbrOfTowedVessel(actionRescueEntities(navActions)),
        nbrOfNoticedVessel = getNbrOfNoticedVessel(actionRescueEntities(navActions)),
        nbrOfHourAtSea = AEMUtils.getDurationInHours(actionRescueEntities(navActions))
    ) {
    }

    companion object {
        fun getNbrOfNoticedVessel(actionRescues: List<ActionRescueEntity?>): Double {
            return actionRescues.filter { it?.isVesselNoticed == true }.size.toDouble();
        }

        fun getNbrOfTowedVessel(actionRescues: List<ActionRescueEntity?>): Double {
            return actionRescues.filter { it?.isVesselTowed == true }.size.toDouble();
        }

        private fun actionRescueEntities(navActions: List<NavActionEntity>): List<ActionRescueEntity?> {
            return navActions.filter { it.rescueAction != null }.map { action -> action.rescueAction }
                .filter { it?.isVesselRescue == true };
        }
    }
}
