package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
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
        nbrOfRescuedOperation = getRescueActions(navActions).size.toDouble(),
        nbrOfTowedVessel = getNbrOfTowedVessel(getRescueActions(navActions)),
        nbrOfNoticedVessel = getNbrOfNoticedVessel(getRescueActions(navActions)),
        nbrOfHourAtSea = AEMUtils.getDurationInHours2(getRescueActions(navActions))
    ) {
    }

    companion object {
        fun getNbrOfNoticedVessel(actionRescues: List<NavActionEntity?>): Double {
            return actionRescues.filter { it?.isVesselNoticed == true }.size.toDouble();
        }

        fun getNbrOfTowedVessel(actionRescues: List<NavActionEntity?>): Double {
            return actionRescues.filter { it?.isVesselTowed == true }.size.toDouble();
        }

        private fun getRescueActions(navActions: List<NavActionEntity>): List<NavActionEntity?> {
            return navActions.filter { it.actionType == ActionType.RESCUE && it.isVesselRescue == true }
        }
    }
}
