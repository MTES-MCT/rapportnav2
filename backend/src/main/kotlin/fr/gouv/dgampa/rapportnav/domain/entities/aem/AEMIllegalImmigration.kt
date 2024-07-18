package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMIllegalImmigration(
    val nbrOfHourInSea: Int? = 0, //3.4.1
    val nbrOfInterceptedVessel: Int? = 0, // 3.4.3
    val nbrOfInterceptedMigrant: Int? = 0, // 3.4.4
    val nbrOfSuspectedSmuggler: Int? = 0 // 3.4.4
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfHourInSea = AEMUtils.getDurationInHours(actionIllegalImmigrationEntities(navActions)).toInt(),
        nbrOfInterceptedVessel = getNbrOfInterceptedVessel(actionIllegalImmigrationEntities(navActions)),
        nbrOfInterceptedMigrant = getNbrOfInterceptedMigrant(actionIllegalImmigrationEntities(navActions)),
        nbrOfSuspectedSmuggler = getNbrOfSuspectedSmuggler(actionIllegalImmigrationEntities(navActions))
    ) {

    }

    companion object {
        fun getNbrOfInterceptedVessel(illegalActions: List<ActionIllegalImmigrationEntity?>): Int {
            return illegalActions.fold(0) { acc, illegalAction -> acc.plus(illegalAction?.nbOfInterceptedVessels!!) }
        }

        fun getNbrOfInterceptedMigrant(illegalActions: List<ActionIllegalImmigrationEntity?>): Int {
            return illegalActions.fold(0) { acc, illegalAction -> acc.plus(illegalAction?.nbOfInterceptedMigrants!!) }
        }

        fun getNbrOfSuspectedSmuggler(illegalActions: List<ActionIllegalImmigrationEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return illegalActions.fold(0) { acc, illegalAction -> acc.plus(illegalAction?.nbOfSuspectedSmugglers!!) }
        }

        private fun actionIllegalImmigrationEntities(navActions: List<NavActionEntity>): List<ActionIllegalImmigrationEntity?> {
            return navActions.filter { it.illegalImmigrationAction != null }
                .map { action -> action.illegalImmigrationAction };
        }
    }
}
