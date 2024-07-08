package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity

data class AEMOutOfMigrationRescue(
    val nbrOfHourInSea: Int, //1.1.1
    val nbrOfRescuedOperation: Int?, //1.1.3.
    val nbrPersonsRescued: Int?, //1.1.4
) {
    constructor(
        actionRescues: List<ActionRescueEntity?>
    ) : this(
        nbrOfHourInSea = AEMOutOfMigrationRescue.processNbrOfHourInSea(actionRescues),
        nbrOfRescuedOperation = actionRescues.size,
        nbrPersonsRescued = AEMOutOfMigrationRescue.processNbrPersonsRescued(actionRescues)
    ) {

    }
    companion object {
        fun processNbrOfHourInSea(actionRescues: List<ActionRescueEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun processNbrPersonsRescued(actionRescues: List<ActionRescueEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }
    }

}
