package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity

data class AEMPollutionControlSurveillance(
    val nbrOfHourInSea: Int, //4.2.1
    val nbrOfSimpleBrassage: Int, // 4.2.3
    val nbrOfDeploymentAction: Int, // 4.2.4
    val nbrOfDetectedPollution: Int, // 4.2.4
    val nbrOfInfractionWithPV: Int, // 4.2.6
    val nbrOfDiversionCarriedOut: Int, // 4.2.7
    val nbrOfPollutionObservedByAuthorizedAgent: Int //4.2.8
) {
    constructor(
        antiPollutionActions: List<ActionAntiPollutionEntity?>,
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourInSea = AEMPollutionControlSurveillance.getNbrOfHourInSea(antiPollutionActions),
        nbrOfSimpleBrassage = AEMPollutionControlSurveillance.getNbrOfSimpleBrassage(antiPollutionActions),
        nbrOfDeploymentAction = AEMPollutionControlSurveillance.getNbrOfDeploymentAction(antiPollutionActions),
        nbrOfDetectedPollution = AEMPollutionControlSurveillance.getNbrOfDetectedPollution(antiPollutionActions),
        nbrOfInfractionWithPV = AEMPollutionControlSurveillance.getNbrOfInfractionWithPV(antiPollutionActions),
        nbrOfDiversionCarriedOut = AEMPollutionControlSurveillance.getNbrOfDiversionCarriedOut(antiPollutionActions),
        nbrOfPollutionObservedByAuthorizedAgent = AEMPollutionControlSurveillance.getNbrOfPollutionObservedByAuthorizedAgent(antiPollutionActions),
    ) {
    }

    companion object {
        fun getNbrOfHourInSea(antiPollutionActions: List<ActionAntiPollutionEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfSimpleBrassage(antiPollutionActions: List<ActionAntiPollutionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfDeploymentAction(antiPollutionActions: List<ActionAntiPollutionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfDetectedPollution(antiPollutionActions: List<ActionAntiPollutionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfInfractionWithPV(antiPollutionActions: List<ActionAntiPollutionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfDiversionCarriedOut(antiPollutionActions: List<ActionAntiPollutionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfPollutionObservedByAuthorizedAgent(antiPollutionActions: List<ActionAntiPollutionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }
    }
}
