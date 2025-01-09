package fr.gouv.dgampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMIllegalImmigration2(
    val nbrOfHourAtSea: Double? = 0.0, //3.4.1
    val nbrOfInterceptedVessel: Double? = 0.0, // 3.4.3
    val nbrOfInterceptedMigrant: Double? = 0.0, // 3.4.4
    val nbrOfSuspectedSmuggler: Double? = 0.0 // 3.4.4
) {
    constructor(
        navActions: List<MissionNavActionEntity>
    ) : this(
        nbrOfHourAtSea = AEMUtils.getDurationInHours2(getIllegalActions(navActions)),
        nbrOfInterceptedVessel = getNbrOfInterceptedVessel(getIllegalActions(navActions)),
        nbrOfInterceptedMigrant = getNbrOfInterceptedMigrant(getIllegalActions(navActions)),
        nbrOfSuspectedSmuggler = getNbrOfSuspectedSmuggler(getIllegalActions(navActions))
    ) {}

    companion object {
        fun getNbrOfInterceptedVessel(illegalActions: List<MissionNavActionEntity?>): Double {
            return illegalActions.fold(0.0) { acc, illegalAction ->
                acc.plus(
                    illegalAction?.nbOfInterceptedVessels ?: 0
                )
            }
        }

        fun getNbrOfInterceptedMigrant(illegalActions: List<MissionNavActionEntity?>): Double {
            return illegalActions.fold(0.0) { acc, illegalAction ->
                acc.plus(
                    illegalAction?.nbOfInterceptedMigrants ?: 0
                )
            }
        }

        fun getNbrOfSuspectedSmuggler(illegalActions: List<MissionNavActionEntity?>): Double {
            return illegalActions.fold(0.0) { acc, illegalAction ->
                acc.plus(
                    illegalAction?.nbOfSuspectedSmugglers ?: 0
                )
            }
        }

        private fun getIllegalActions(navActions: List<MissionNavActionEntity>): List<MissionNavActionEntity?> {
            return navActions.filter { it.actionType == ActionType.ILLEGAL_IMMIGRATION }
        }
    }
}
