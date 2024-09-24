package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMOutOfMigrationRescue(
    val nbrOfHourAtSea: Double? = 0.0, //1.1.1
    val nbrOfRescuedOperation: Double? = 0.0, //1.1.3.
    val nbrPersonsRescued: Double? = 0.0, //1.1.4
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfRescuedOperation = actionRescueEntities(navActions).size.toDouble(),
        nbrPersonsRescued = getNbrPersonsRescued(actionRescueEntities(navActions)),
        nbrOfHourAtSea = AEMUtils.getDurationInHours(actionRescueEntities(navActions)),
    ) {
    }

    companion object {
        fun getNbrPersonsRescued(actionRescues: List<ActionRescueEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue -> acc.plus(actionRescue?.numberPersonsRescued!!) }
        }

        private fun actionRescueEntities(navActions: List<NavActionEntity>): List<ActionRescueEntity?> {
            return navActions.filter { it.rescueAction != null }.map { action -> action.rescueAction }
                .filter { it?.isMigrationRescue != true };
        }
    }
}
