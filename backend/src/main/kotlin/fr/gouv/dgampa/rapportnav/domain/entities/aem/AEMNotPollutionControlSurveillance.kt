package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

data class AEMNotPollutionControlSurveillance(
    val nbrOfHourAtSea: Int? = 0, //4.1.1
    val nbrOfAction: Int? = 0, // 4.1.3
    val nbrOfInfraction: Int? = 0, // 4.1.4
    val nbrOfInfractionWithNotice: Int? = 0 // 4.1.5
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = AEMUtils.getEnvDurationInHours(notPollutionActionEntities(envActions)).toInt(),
        nbrOfAction = notPollutionActionEntities(envActions).size,
        nbrOfInfraction = getNbrOfInfraction(notPollutionActionEntities(envActions)),
        nbrOfInfractionWithNotice = getNbrOfInfractionWithNotice(notPollutionActionEntities(envActions))
    ) {

    }

    companion object {

        fun getNbrOfInfraction(notPollutionActions: List<ExtendedEnvActionEntity?>): Int {
            return notPollutionActions
                .fold(0) { acc, c -> acc.plus(c?.controlAction?.action?.infractions?.size ?: 0) }
        }

        fun getNbrOfInfractionWithNotice(notPollutionActions: List<ExtendedEnvActionEntity?>): Int {
            return notPollutionActions.fold(0) { acc, c ->
                acc.plus(
                    c?.controlAction?.action?.infractions?.filter { it.formalNotice == FormalNoticeEnum.YES }?.size ?: 0
                )
            }
        }

        private fun notPollutionActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {
            val illicitRejects = listOf(19, 102);
            return envActions.filter {
                it?.controlAction?.action?.controlPlans?.map { c -> c.themeId }?.containsAll(illicitRejects) != true ||
                    it?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.containsAll(illicitRejects) != false
            }
        }

    }
}
