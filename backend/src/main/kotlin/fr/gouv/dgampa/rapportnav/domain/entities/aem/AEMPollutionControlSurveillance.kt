package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMPollutionControlSurveillance(
    val nbrOfHourAtSea: Double? = 0.0, //4.2.1
    val nbrOfSimpleBrewingOperation: Double? = 0.0, // 4.2.3
    val nbrOfAntiPolDeviceDeployed: Double? = 0.0, // 4.2.4
    val nbrOfInfraction: Double? = 0.0, // 4.2.5
    val nbrOfInfractionWithNotice: Double? = 0.0, // 4.2.6
    val nbrOfDiversionCarriedOut: Double? = 0.0, // 4.2.7
    val nbrOfPollutionObservedByAuthorizedAgent: Double? = 0.0 //4.2.8
) {
    constructor(
        navActions: List<NavActionEntity>,
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = getNbrOfHourAtSea(
            illicitRejectActionEntities(envActions),
            actionAntiPollutionEntities(navActions)
        ),
        nbrOfSimpleBrewingOperation = getNbrOfSimpleBrewingOperation(actionAntiPollutionEntities(navActions)),
        nbrOfAntiPolDeviceDeployed = getNbrOfAntiPolDeviceDeployed(actionAntiPollutionEntities(navActions)),
        nbrOfInfraction = getNbrOfInfraction(illicitRejectActionEntities(envActions)),
        nbrOfInfractionWithNotice = getNbrOfInfractionWithNotice(illicitRejectActionEntities(envActions)),
        nbrOfDiversionCarriedOut = getNbrOfDiversionCarriedOut(
            illicitRejectActionEntities(envActions),
            actionAntiPollutionEntities(navActions)
        ),
        nbrOfPollutionObservedByAuthorizedAgent = getNbrOfPollutionObservedByAuthorizedAgent(
            illicitRejectActionEntities(envActions),
            actionAntiPollutionEntities(navActions)
        ),
    ) {
    }

    companion object {
        fun getNbrOfHourAtSea(
            illicitRejectsActions: List<ExtendedEnvActionEntity?>,
            antiPollutionActions: List<ActionAntiPollutionEntity?>
        ): Double {
            val nbrOfHourAntiPollution = AEMUtils.getDurationInHours(antiPollutionActions);
            val nbrEnvOfHourAntiPollution = AEMUtils.getEnvDurationInHours(illicitRejectsActions);
            return nbrOfHourAntiPollution.plus(nbrEnvOfHourAntiPollution);
        }

        fun getNbrOfSimpleBrewingOperation(antiPollutionActions: List<ActionAntiPollutionEntity?>): Double {
            return antiPollutionActions.filter { it?.isSimpleBrewingOperationDone == true }.size.toDouble();
        }

        fun getNbrOfAntiPolDeviceDeployed(antiPollutionActions: List<ActionAntiPollutionEntity?>): Double {
            return antiPollutionActions.filter { it?.isAntiPolDeviceDeployed == true }.size.toDouble();
        }

        fun getNbrOfInfraction(illicitRejectsActions: List<ExtendedEnvActionEntity?>): Double {
            return illicitRejectsActions.fold(0.0) { acc, envAction ->
                acc.plus(
                    envAction?.controlAction?.action?.infractions?.size ?: 0
                )
            }
        }

        fun getNbrOfInfractionWithNotice(illicitRejectsActions: List<ExtendedEnvActionEntity?>): Double {
            return illicitRejectsActions.fold(0.0) { acc, envAction ->
                acc.plus(
                    envAction?.controlAction?.action?.infractions?.filter { it.formalNotice == FormalNoticeEnum.YES }?.size
                        ?: 0
                )
            }
        }

        fun getNbrOfDiversionCarriedOut(
            illicitRejectsActions: List<ExtendedEnvActionEntity?>,
            antiPollutionActions: List<ActionAntiPollutionEntity?>
        ): Double {
            val envNbrOfDiversionCarriedOut = 0.0; //TODO: diversionCarriedOut from Env
            val navNbrOfDiversionCarriedOut = antiPollutionActions.filter { it?.diversionCarriedOut == true }.size;

            return envNbrOfDiversionCarriedOut.plus(navNbrOfDiversionCarriedOut);
        }

        fun getNbrOfPollutionObservedByAuthorizedAgent(
            illicitRejectsActions: List<ExtendedEnvActionEntity?>,
            antiPollutionActions: List<ActionAntiPollutionEntity?>
        ): Double {
            val envNbrOfPollutionObservedByAuthorizedAgent = 0.0; //TODO: pollutionObservedByAuthorizedAgent from env
            val navNbrOfPollutionObservedByAuthorizedAgent =
                antiPollutionActions.filter { it?.pollutionObservedByAuthorizedAgent == true }.size;
            return envNbrOfPollutionObservedByAuthorizedAgent.plus(navNbrOfPollutionObservedByAuthorizedAgent);
        }

        private fun actionAntiPollutionEntities(navActions: List<NavActionEntity>): List<ActionAntiPollutionEntity?> {
            return navActions.filter { it.antiPollutionAction != null }.map { action -> action.antiPollutionAction };
        }

        private fun illicitRejectActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {
            val illicitRejects = listOf(19, 102);
            return envActions.filter {
                it?.controlAction?.action?.controlPlans?.map { c -> c.themeId }?.intersect(illicitRejects)
                    ?.isEmpty() == false ||
                    it?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.intersect(illicitRejects)?.isEmpty() == false
            };
        }
    }
}
