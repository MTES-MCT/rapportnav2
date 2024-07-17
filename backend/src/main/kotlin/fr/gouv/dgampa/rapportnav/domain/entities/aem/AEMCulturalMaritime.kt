package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*

data class AEMCulturalMaritime (
    val nbrOfHourInSea: Int, //4.4.1
    val nbrOfScientificOperation: Int, // 4.4.2
    val nbrOfBCMPoliceOperation: Int, // 4.4.3
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourInSea = AEMCulturalMaritime.getNbrOfHourInSea(envActions),
        nbrOfScientificOperation = AEMCulturalMaritime.getNbrOfScientificOperation(envActions),
        nbrOfBCMPoliceOperation = AEMCulturalMaritime.getNbrOfBCMPoliceOperation(envActions),
    ) {
    }
    companion object {
        fun getNbrOfHourInSea(envActions: List<ExtendedEnvActionEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfScientificOperation(envActions: List<ExtendedEnvActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfBCMPoliceOperation(envActions: List<ExtendedEnvActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

    }
}

