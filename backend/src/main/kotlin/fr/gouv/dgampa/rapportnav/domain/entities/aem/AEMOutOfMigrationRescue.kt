package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMOutOfMigrationRescue(
    val nbrOfHourAtSea: Double? = 0.0, //1.1.1
    val nbrOfRescuedOperation: Double? = 0.0, //1.1.3.
    val nbrPersonsRescued: Double? = 0.0, //1.1.4
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfRescuedOperation = getMigrationRescueActions(navActions).size.toDouble(),
        nbrPersonsRescued = getNbrPersonsRescued(getMigrationRescueActions(navActions)),
        nbrOfHourAtSea = AEMUtils.getDurationInHours2(getMigrationRescueActions(navActions)),
    ) {
    }

    companion object {
        fun getNbrPersonsRescued(actionRescues: List<NavActionEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue -> acc.plus(actionRescue?.numberPersonsRescued ?: 0) }
        }

        private fun getMigrationRescueActions(navActions: List<NavActionEntity>): List<NavActionEntity?> {
            return navActions.filter { it.actionType == ActionType.RESCUE }
                .filter { it.isMigrationRescue != true };
        }
    }
}
