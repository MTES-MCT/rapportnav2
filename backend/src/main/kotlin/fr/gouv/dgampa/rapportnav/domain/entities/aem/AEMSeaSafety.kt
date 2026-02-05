package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMSeaSafety(
    val nbrOfHourAtSea: Double? = 0.0, // 5.1
    val nbrOfHourPublicOrder: Double? = 0.0, // 5.3
    val nbrOfPublicOrderOperation: Double? = 0.0, // 5.4
) {
    constructor(
        navActions: List<MissionNavActionEntity>
    ) : this(
        nbrOfHourAtSea = getNbrOfHourAtSea(navActions),
        nbrOfPublicOrderOperation = getPublicOrderActions(navActions).size.toDouble(),
        nbrOfHourPublicOrder = AEMUtils.getDurationInHours2(getPublicOrderActions(navActions)),
    ) {
    }

    companion object {
        fun getNbrOfHourAtSea(
            navActions: List<MissionNavActionEntity>
        ): Double {
            val otherActions = getOtherTargetActions(navActions)
            val publicOrderActions = getPublicOrderActions(navActions)
            return 0.0.plus(AEMUtils.getDurationInHours2(otherActions))
                .plus(AEMUtils.getDurationInHours2(publicOrderActions))
        }

        private fun getPublicOrderActions(navActions: List<MissionNavActionEntity>): List<MissionNavActionEntity?> {
            return navActions.filter { listOf(ActionType.PUBLIC_ORDER).contains(it.actionType) }
        }

        private fun getOtherTargetActions(navActions: List<MissionNavActionEntity>): List<MissionNavActionEntity?> {
            return navActions.filter { listOf(ActionType.VIGIMER,ActionType.BAAEM_PERMANENCE, ActionType.NAUTICAL_EVENT).contains(it.actionType) }
        }
    }
}
