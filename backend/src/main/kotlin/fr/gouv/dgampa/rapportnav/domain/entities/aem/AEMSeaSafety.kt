package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMSeaSafety(
    val nbrOfHourAtSea: Double? = 0.0, // 5.1
    val nbrOfHourPublicOrder: Double? = 0.0, // 5.3
    val nbrOfPublicOrderOperation: Double? = 0.0, // 5.4
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfHourAtSea = getNbrOfHourAtSea(navActions),
        nbrOfPublicOrderOperation = actionPublicOrderEntities(navActions).size.toDouble(),
        nbrOfHourPublicOrder = AEMUtils.getDurationInHours(actionPublicOrderEntities(navActions)),
    ) {
    }

    companion object {
        fun getNbrOfHourAtSea(
            navActions: List<NavActionEntity>
        ): Double {
            val vigimerActions = actionVigimerEntities(navActions)
            val publicActions = actionPublicOrderEntities(navActions)
            val nauticalActions = actionNauticalEventEntities(navActions)
            val baaemActions = actionBAAEMPermanenceEntities(navActions)

            return 0.0.plus(AEMUtils.getDurationInHours(baaemActions))
                .plus(AEMUtils.getDurationInHours(publicActions))
                .plus(AEMUtils.getDurationInHours(vigimerActions))
                .plus(AEMUtils.getDurationInHours(nauticalActions))
        }

        private fun actionPublicOrderEntities(navActions: List<NavActionEntity>): List<ActionPublicOrderEntity?> {
            return navActions.filter { it.publicOrderAction != null }
                .map { action -> action.publicOrderAction }
        }

        private fun actionBAAEMPermanenceEntities(navActions: List<NavActionEntity>): List<ActionBAAEMPermanenceEntity?> {
            return navActions.filter { it.baaemPermanenceAction != null }
                .map { action -> action.baaemPermanenceAction }
        }

        private fun actionNauticalEventEntities(navActions: List<NavActionEntity>): List<ActionNauticalEventEntity?> {
            return navActions.filter { it.nauticalEventAction != null }
                .map { action -> action.nauticalEventAction };
        }

        private fun actionVigimerEntities(navActions: List<NavActionEntity>): List<ActionVigimerEntity?> {
            return navActions.filter { it.vigimerAction != null }
                .map { action -> action.vigimerAction };
        }
    }
}
