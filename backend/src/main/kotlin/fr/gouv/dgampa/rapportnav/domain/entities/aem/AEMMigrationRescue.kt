package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import java.time.ZonedDateTime

data class AEMMigrationRescue(
    val nbrOfHourInSea: Int, // 1.2.1
    val nbrOfOperation: Int?,// 1.2.3
    val nbrOfVesselsTrackedWithoutIntervention: Int, //1.2.4
    val nbrAssistedVesselsReturningToShore: Int?, //1.2.5
    val nbrPersonsRescued: Int?,
    val nbrOfRescuedOperation: Int?,// 1.2.3
) {
    constructor(
        actionRescues: List<ActionRescueEntity?>
    ) : this(
        nbrOfHourInSea = AEMMigrationRescue.processNbrOfHourInSea(actionRescues),
        nbrPersonsRescued = AEMMigrationRescue.processNbrPersonsRescued(actionRescues),
        nbrOfOperation = 0, //TODO: Define correctly what that means
        nbrOfVesselsTrackedWithoutIntervention = AEMMigrationRescue.processNbrOfVesselsTrackedWithoutIntervention(actionRescues),
        nbrAssistedVesselsReturningToShore = AEMMigrationRescue.processAssistedVesselsReturningToShore(actionRescues),
        nbrOfRescuedOperation = actionRescues.size,
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

        fun processNbrOfVesselsTrackedWithoutIntervention(actionRescues: List<ActionRescueEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun processAssistedVesselsReturningToShore(actionRescues: List<ActionRescueEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }
    }

}
