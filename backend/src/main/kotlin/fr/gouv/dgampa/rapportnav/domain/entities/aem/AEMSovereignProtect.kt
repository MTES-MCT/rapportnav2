package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*

data class AEMSovereignProtect(
    val nbrOfHourInSea: Int, // 7.1
    val nbrOfReconizedVessel: Int, // 7.3
    val nbrOfControlledVessel: Int, // 7.4
) {
    constructor(
        anchoredActions: List<ActionStatusEntity?>,
        navigationActions: List<ActionStatusEntity?>
    ) : this(
        nbrOfHourInSea = AEMSovereignProtect.getNbrOfHourInSea(anchoredActions, navigationActions),
        nbrOfReconizedVessel = AEMSovereignProtect.getNbrOfReconizedVessel(anchoredActions, navigationActions),
        nbrOfControlledVessel = AEMSovereignProtect.getNbrOfControlledVessel(anchoredActions, navigationActions),
    ) {
    }

    companion object {
        fun getNbrOfHourInSea(
            anchoredActions: List<ActionStatusEntity?>,
            navigationActions: List<ActionStatusEntity?>
        ): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfReconizedVessel(
            anchoredActions: List<ActionStatusEntity?>,
            navigationActions: List<ActionStatusEntity?>
        ): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfControlledVessel(
            anchoredActions: List<ActionStatusEntity?>,
            navigationActions: List<ActionStatusEntity?>
        ): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }
    }
}
