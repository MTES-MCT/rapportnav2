package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMNotPollutionControlSurveillance(
    val nbrOfHourAtSea: Double? = 0.0, //4.1.1
    val nbrOfAction: Double? = 0.0, // 4.1.3
    val nbrOfInfraction: Double? = 0.0, // 4.1.4
    val nbrOfInfractionWithNotice: Double? = 0.0 // 4.1.5
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = AEMUtils.getEnvDurationInHours(notPollutionActionEntities(envActions)),
        nbrOfAction = notPollutionActionEntities(envActions).size.toDouble(),
        nbrOfInfraction = getNbrOfInfraction(notPollutionActionEntities(envActions)),
        nbrOfInfractionWithNotice = getNbrOfInfractionWithNotice(notPollutionActionEntities(envActions))
    ) {

    }

    companion object {

        fun getNbrOfInfraction(notPollutionActions: List<ExtendedEnvActionEntity?>): Double {
            return notPollutionActions
                .fold(0.0) { acc, c ->
                    acc.plus(
                        c?.controlAction?.action?.infractions?.flatMap { it.natinf ?: listOf() }?.size ?: 0
                    )
                }
        }


        fun getNbrOfInfractionWithNotice(notPollutionActions: List<ExtendedEnvActionEntity?>): Double {
            return notPollutionActions.fold(0.0) { acc, c ->
                acc.plus(
                    c?.controlAction?.action?.infractions?.filter { it.infractionType == InfractionTypeEnum.WITH_REPORT }?.size
                        ?: 0
                )
            }
        }

        private fun notPollutionActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {
            val illicitRejects = listOf(19, 102);
            return envActions.filter {
                it?.controlAction?.action?.controlPlans?.map { c -> c.themeId }?.intersect(
                    illicitRejects
                )?.isEmpty() == true ||
                    it?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.intersect(illicitRejects)?.isEmpty() == true
            }
        }

    }
}

