package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMMigrationRescue(
    val nbrOfHourAtSea: Int? = 0, // 1.2.1
    val nbrOfOperation: Int? = 0,// 1.2.3
    val nbrOfVesselsTrackedWithoutIntervention: Int? = 0, //1.2.4
    val nbrAssistedVesselsReturningToShore: Int? = 0, //1.2.5
    val nbrOfRescuedOperation: Int? = 0, //1.2/6
    val nbrPersonsRescued: Int? = 0,// 1.2.7
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfRescuedOperation = actionRescueEntities(navActions).size,
        nbrOfHourAtSea = AEMUtils.getDurationInHours(actionRescueEntities(navActions)).toInt(),
        nbrPersonsRescued = getNbrPersonsRescued(actionRescueEntities(navActions)),
        nbrOfOperation = actionRescueEntities(navActions).size, //TODO: Define correctly what that means
        nbrAssistedVesselsReturningToShore = getAssistedVesselsReturningToShore(actionRescueEntities(navActions)),
        nbrOfVesselsTrackedWithoutIntervention = getNbrOfVesselsTrackedWithoutIntervention(actionRescueEntities(navActions))

    ) {

    }

    companion object {
        fun getNbrPersonsRescued(actionRescues: List<ActionRescueEntity?>): Int {
            return actionRescues.fold(0) { acc, actionRescue -> acc.plus(actionRescue?.numberPersonsRescued!!) }
        }

        fun getNbrOfVesselsTrackedWithoutIntervention(actionRescues: List<ActionRescueEntity?>): Int {
            return actionRescues.fold(0) { acc, actionRescue -> acc.plus(actionRescue?.nbOfVesselsTrackedWithoutIntervention!!) }
        }

        fun getAssistedVesselsReturningToShore(actionRescues: List<ActionRescueEntity?>): Int {
            return actionRescues.fold(0) { acc, actionRescue -> acc.plus(actionRescue?.nbAssistedVesselsReturningToShore!!) }
        }

        private fun actionRescueEntities(navActions: List<NavActionEntity>): List<ActionRescueEntity?> {
            return navActions.filter { it.rescueAction != null }.map { action -> action.rescueAction }.filter { it?.isMigrationRescue == true };
        }
    }
}
