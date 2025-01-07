package fr.gouv.dgampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMMigrationRescue2(
    val nbrOfHourAtSea: Double? = 0.0, // 1.2.1
    val nbrOfOperation: Double? = 0.0,// 1.2.3
    val nbrOfVesselsTrackedWithoutIntervention: Double? = 0.0, //1.2.4
    val nbrAssistedVesselsReturningToShore: Double? = 0.0, //1.2.5
    val nbrOfRescuedOperation: Double? = 0.0, //1.2.6
    val nbrPersonsRescued: Double? = 0.0,// 1.2.7
) {
    constructor(
        navActions: List<MissionNavActionEntity>
    ) : this(
        nbrOfRescuedOperation = getMigrationRescueActions(navActions).size.toDouble(),
        nbrOfHourAtSea = AEMUtils.getDurationInHours2(getMigrationRescueActions(navActions)),
        nbrPersonsRescued = getNbrPersonsRescued(getMigrationRescueActions(navActions)),
        nbrOfOperation = getMigrationRescueActions(navActions).size.toDouble(), //TODO: Define correctly what that means
        nbrAssistedVesselsReturningToShore = getAssistedVesselsReturningToShore(getMigrationRescueActions(navActions)),
        nbrOfVesselsTrackedWithoutIntervention = getNbrOfVesselsTrackedWithoutIntervention(
            getMigrationRescueActions(
                navActions
            )
        )

    )

    companion object {
        fun getNbrPersonsRescued(actionRescues: List<MissionNavActionEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue -> acc.plus(actionRescue?.numberPersonsRescued ?: 0) }
        }

        fun getNbrOfVesselsTrackedWithoutIntervention(actionRescues: List<MissionNavActionEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue ->
                acc.plus(
                    actionRescue?.nbOfVesselsTrackedWithoutIntervention ?: 0
                )
            }
        }

        fun getAssistedVesselsReturningToShore(actionRescues: List<MissionNavActionEntity?>): Double {
            return actionRescues.fold(0.0) { acc, actionRescue ->
                acc.plus(
                    actionRescue?.nbAssistedVesselsReturningToShore ?: 0
                )
            }
        }

        private fun getMigrationRescueActions(navActions: List<MissionNavActionEntity>): List<MissionNavActionEntity?> {
            return navActions.filter { it.actionType == ActionType.RESCUE }
                .filter { it.isMigrationRescue == true }
        }
    }
}
