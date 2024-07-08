package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity

data class AEMNotPollutionControlSurveillance(
    val nbrOfHourInSea: Int, //4.1.1
    val nbrOfAction: Int, // 4.1.3
    val nbrOfInfraction: Int, // 4.1.4
    val nbrOfInfractionWithPV: Int // 4.1.5
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourInSea = AEMNotPollutionControlSurveillance.getNbrOfHourInSea(envActions),
        nbrOfAction = AEMNotPollutionControlSurveillance.getNbrOfAction(envActions),
        nbrOfInfraction = AEMNotPollutionControlSurveillance.getNbrOfInfraction(envActions),
        nbrOfInfractionWithPV = AEMNotPollutionControlSurveillance.getNbrOfInfractionWithPV(envActions)
    ) {

    }

    companion object {
        fun getNbrOfHourInSea(envActions: List<ExtendedEnvActionEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfAction(envActions: List<ExtendedEnvActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfInfraction(envActions: List<ExtendedEnvActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfInfractionWithPV(envActions: List<ExtendedEnvActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

    }
}
