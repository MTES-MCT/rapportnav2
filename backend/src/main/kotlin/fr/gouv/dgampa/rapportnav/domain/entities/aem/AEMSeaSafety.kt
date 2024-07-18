package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMSeaSafety(
    val nbrOfHourInSea: Int? = 0, // 5.1
    val nbrOfHourPublicOrder: Int? = 0, // 5.3
    val nbrOfPublicOrderOperation: Int? = 0, // 5.4
) {
    constructor(
        navActions: List<NavActionEntity>
    ) : this(
        nbrOfHourInSea = getNbrOfHourInSea(navActions).toInt(),
        nbrOfPublicOrderOperation = actionPublicOrderEntities(navActions).size,
        nbrOfHourPublicOrder = AEMUtils.getDurationInHours(actionPublicOrderEntities(navActions)).toInt(),
    ) {
    }

    companion object {
        fun getNbrOfHourInSea(
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
