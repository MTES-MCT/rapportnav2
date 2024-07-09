package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionIllegalImmigrationEntity

data class AEMIllegalImmigration(
    val nbrOfHourInSea: Int, //3.4.1
    val nbrOfInterceptedShip: Int, // 3.4.3
    val nbrOfInterceptedMigrant: Int, // 3.4.4
    val nbrOfAllegedTrafficker: Int // 3.4.4
) {
    constructor(
        illegalActions: List<ActionIllegalImmigrationEntity?>
    ) : this(
        nbrOfHourInSea = AEMIllegalImmigration.getNbrOfHourInSea(illegalActions),
        nbrOfInterceptedShip = AEMIllegalImmigration.getNbrOfInterceptedShip(illegalActions),
        nbrOfInterceptedMigrant = AEMIllegalImmigration.getNbrOfInterceptedMigrant(illegalActions),
        nbrOfAllegedTrafficker = AEMIllegalImmigration.getNbrOfAllegedTrafficker(illegalActions)
    ) {

    }

    companion object {
        fun getNbrOfHourInSea(illegalActions: List<ActionIllegalImmigrationEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfInterceptedShip(illegalActions: List<ActionIllegalImmigrationEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfInterceptedMigrant(illegalActions: List<ActionIllegalImmigrationEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfAllegedTrafficker(illegalActions: List<ActionIllegalImmigrationEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

    }
}
