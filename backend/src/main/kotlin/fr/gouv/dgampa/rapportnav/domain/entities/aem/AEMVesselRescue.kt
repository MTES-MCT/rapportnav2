package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMVesselRescue(
    val nbrOfHourAtSea: Int? = 0, // 2.1
    val nbrOfRescuedOperation: Int? = 0,// 2.3
    val nbrOfNoticedVessel: Int? = 0, // 2.4
    val nbrOfTowedVessel: Int? = 0, //2.7
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfRescuedOperation = actionRescueEntities(navActions).size,
        nbrOfTowedVessel = getNbrOfTowedVessel(actionRescueEntities(navActions)),
        nbrOfNoticedVessel = getNbrOfNoticedVessel(actionRescueEntities(navActions)),
        nbrOfHourAtSea = AEMUtils.getDurationInHours(actionRescueEntities(navActions)).toInt()
    ) {
    }

    companion object {
        fun getNbrOfNoticedVessel(actionRescues: List<ActionRescueEntity?>): Int {
            return actionRescues.filter { it?.isVesselNoticed == true }.size;
        }

        fun getNbrOfTowedVessel(actionRescues: List<ActionRescueEntity?>): Int {
            return actionRescues.filter { it?.isVesselTowed == true }.size;
        }

        private fun actionRescueEntities(navActions: List<NavActionEntity>): List<ActionRescueEntity?> {
            return navActions.filter { it.rescueAction != null }.map { action -> action.rescueAction }
                .filter { it?.isVesselRescue == true };
        }
    }
}
