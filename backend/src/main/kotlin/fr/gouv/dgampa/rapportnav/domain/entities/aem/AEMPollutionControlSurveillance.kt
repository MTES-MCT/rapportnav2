package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
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
        navActions: List<MissionNavActionEntity>,
        envActions: List<MissionEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = getNbrOfHourAtSea(
            getIllicitRejectActions(envActions),
            getAntiPollutionActions(navActions)
        ),
        nbrOfSimpleBrewingOperation = getNbrOfSimpleBrewingOperation(getAntiPollutionActions(navActions)),
        nbrOfAntiPolDeviceDeployed = getNbrOfAntiPolDeviceDeployed(getAntiPollutionActions(navActions)),
        nbrOfInfraction = getNbrOfInfraction(getIllicitRejectActions(envActions)),
        nbrOfInfractionWithNotice = getNbrOfInfractionWithNotice(getIllicitRejectActions(envActions)),
        nbrOfDiversionCarriedOut = getNbrOfDiversionCarriedOut(
            getIllicitRejectActions(envActions),
            getAntiPollutionActions(navActions)
        ),
        nbrOfPollutionObservedByAuthorizedAgent = getNbrOfPollutionObservedByAuthorizedAgent(
            getIllicitRejectActions(envActions),
            getAntiPollutionActions(navActions)
        ),
    ) {
    }

    companion object {
        fun getNbrOfHourAtSea(
            illicitRejectsActions: List<MissionEnvActionEntity?>,
            antiPollutionActions: List<MissionNavActionEntity?>
        ): Double {
            val nbrOfHourAntiPollution = AEMUtils.getDurationInHours2(antiPollutionActions);
            val nbrEnvOfHourAntiPollution = AEMUtils.getDurationInHours2(illicitRejectsActions);
            return nbrOfHourAntiPollution.plus(nbrEnvOfHourAntiPollution);
        }

        fun getNbrOfSimpleBrewingOperation(antiPollutionActions: List<MissionNavActionEntity?>): Double {
            return antiPollutionActions.filter { it?.isSimpleBrewingOperationDone == true }.size.toDouble();
        }

        fun getNbrOfAntiPolDeviceDeployed(antiPollutionActions: List<MissionNavActionEntity?>): Double {
            return antiPollutionActions.filter { it?.isAntiPolDeviceDeployed == true }.size.toDouble();
        }

        fun getNbrOfInfraction(illicitRejectsActions: List<MissionEnvActionEntity?>,): Double {
            return illicitRejectsActions.fold(0.0) { acc, envAction ->
                acc.plus(
                    envAction?.envInfractions?.flatMap { it.natinf ?: listOf() }?.size ?: 0
                )
            }
        }

        fun getNbrOfInfractionWithNotice(illicitRejectsActions: List<MissionEnvActionEntity?>,): Double {
            return illicitRejectsActions.fold(0.0) { acc, envAction ->
                acc.plus(
                    envAction?.envInfractions?.filter { it.infractionType == InfractionTypeEnum.WITH_REPORT }?.size
                        ?: 0
                )
            }
        }
        //it.infractionType == InfractionTypeEnum.WITH_REPORT

        fun getNbrOfDiversionCarriedOut(
            illicitRejectsActions: List<MissionEnvActionEntity?>,
            antiPollutionActions: List<MissionNavActionEntity?>
        ): Double {
            val envNbrOfDiversionCarriedOut = 0.0; //TODO: diversionCarriedOut from Env
            val navNbrOfDiversionCarriedOut = antiPollutionActions.filter { it?.diversionCarriedOut == true }.size;

            return envNbrOfDiversionCarriedOut.plus(navNbrOfDiversionCarriedOut);
        }

        fun getNbrOfPollutionObservedByAuthorizedAgent(
            illicitRejectsActions: List<MissionEnvActionEntity?>,
            antiPollutionActions: List<MissionNavActionEntity?>
        ): Double {
            val envNbrOfPollutionObservedByAuthorizedAgent = 0.0; //TODO: pollutionObservedByAuthorizedAgent from env
            val navNbrOfPollutionObservedByAuthorizedAgent =
                antiPollutionActions.filter { it?.pollutionObservedByAuthorizedAgent == true }.size;
            return envNbrOfPollutionObservedByAuthorizedAgent.plus(navNbrOfPollutionObservedByAuthorizedAgent);
        }

        private fun getAntiPollutionActions(navActions: List<MissionNavActionEntity>): List<MissionNavActionEntity?> {
            return navActions.filter { it.actionType == ActionType.ANTI_POLLUTION };
        }

        private fun getIllicitRejectActions(envActions: List<MissionEnvActionEntity?>): List<MissionEnvActionEntity?> {
            val illicitRejects = listOf(19, 102);
            return envActions.filter {
                it?.themes?.map { t -> t.id }?.intersect(illicitRejects)
                    ?.isEmpty() == false
            };
        }
    }
}
