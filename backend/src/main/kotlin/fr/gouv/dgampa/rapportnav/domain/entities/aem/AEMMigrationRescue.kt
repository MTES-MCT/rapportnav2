package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMMigrationRescue(
    val nbrOfHourAtSea: Double? = 0.0, // 1.2.1
    val nbrOfOperation: Double? = 0.0,// 1.2.3
    val nbrOfVesselsTrackedWithoutIntervention: Double? = 0.0, //1.2.4
    val nbrAssistedVesselsReturningToShore: Double? = 0.0, //1.2.5
    val nbrOfRescuedOperation: Double? = 0.0, //1.2.6
    val nbrPersonsRescued: Double? = 0.0,// 1.2.7
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfRescuedOperation = actionRescueEntities(navActions).size.toDouble(),
        nbrOfHourAtSea = AEMUtils.getDurationInHours(actionRescueEntities(navActions)),
        nbrPersonsRescued = getNbrPersonsRescued(actionRescueEntities(navActions)),
        nbrOfOperation = actionRescueEntities(navActions).size.toDouble(), //TODO: Define correctly what that means
        nbrAssistedVesselsReturningToShore = getAssistedVesselsReturningToShore(actionRescueEntities(navActions)),
        nbrOfVesselsTrackedWithoutIntervention = getNbrOfVesselsTrackedWithoutIntervention(
            actionRescueEntities(
                navActions
            )
        )

    ) {

    }

    companion object {
        fun getNbrPersonsRescued(actionRescues: List<ActionRescueEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue -> acc.plus(actionRescue?.numberPersonsRescued ?: 0) }
        }

        fun getNbrOfVesselsTrackedWithoutIntervention(actionRescues: List<ActionRescueEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue ->
                acc.plus(
                    actionRescue?.nbOfVesselsTrackedWithoutIntervention ?: 0
                )
            }
        }

        fun getAssistedVesselsReturningToShore(actionRescues: List<ActionRescueEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue ->
                acc.plus(
                    actionRescue?.nbAssistedVesselsReturningToShore ?: 0
                )
            }
        }

        private fun actionRescueEntities(navActions: List<NavActionEntity>): List<ActionRescueEntity?> {
            return navActions.filter { it.rescueAction != null }.map { action -> action.rescueAction }
                .filter { it?.isMigrationRescue == true };
        }
    }
}
