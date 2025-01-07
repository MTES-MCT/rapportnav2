package fr.gouv.dgampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMNotPollutionControlSurveillance2(
    val nbrOfHourAtSea: Double? = 0.0, //4.1.1
    val nbrOfAction: Double? = 0.0, // 4.1.3
    val nbrOfInfraction: Double? = 0.0, // 4.1.4
    val nbrOfInfractionWithNotice: Double? = 0.0 // 4.1.5
) {
    constructor(
        envActions: List<MissionEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = AEMUtils.getDurationInHours2(getNotPollutionActions(envActions)),
        nbrOfAction = getNotPollutionActions(envActions).size.toDouble(),
        nbrOfInfraction = getNbrOfInfraction(getNotPollutionActions(envActions)),
        nbrOfInfractionWithNotice = getNbrOfInfractionWithNotice(getNotPollutionActions(envActions))
    ) {

    }

    companion object {

        fun getNbrOfInfraction(notPollutionActions: List<MissionEnvActionEntity?>): Double {
            return notPollutionActions
                .fold(0.0) { acc, c ->
                    acc.plus(c?.envInfractions?.flatMap { it.natinf ?: listOf() }?.size ?: 0)
                }
        }

        fun getNbrOfInfractionWithNotice(notPollutionActions: List<MissionEnvActionEntity?>): Double {
            return notPollutionActions.fold(0.0) { acc, c ->
                acc.plus(
                    c?.envInfractions?.filter { it.infractionType == InfractionTypeEnum.WITH_REPORT }?.size ?: 0
                )
            }
        }

        private fun getNotPollutionActions(envActions: List<MissionEnvActionEntity?>): List<MissionEnvActionEntity?> {
            val illicitRejects = listOf(19, 102);
            return envActions.filter {
                it?.controlPlans?.map { c -> c.themeId }?.intersect(illicitRejects)?.isEmpty() == true
            }
        }

    }
}

