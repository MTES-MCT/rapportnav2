package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*

data class AEMIllegalFish(
    val nbrOfHourInSea: Int, //4.3.1
    val nbrOfPolFishAction: Int, // 4.1.3
    val nbrOfTargetedVessel: Int, // 4.1.4
    val nbrOfInfraction: Int, // 4.1.5
    val nbrOfInfractionWithPV: Int, // 4.1.5
    val nbrOfTowedVessel: Int, // 4.3.8
    val quantityOfFish: Int //4.3.9
) {
    constructor(
        fishActions: List<ExtendedFishActionEntity?>
    ) : this(
        nbrOfHourInSea = AEMIllegalFish.getNbrOfHourInSea(fishActions),
        nbrOfPolFishAction = AEMIllegalFish.getNbrOfPolFishAction(fishActions),
        nbrOfTargetedVessel = AEMIllegalFish.getNbrOfTargetedVessel(fishActions),
        nbrOfInfraction = AEMIllegalFish.getNbrOfInfraction(fishActions),
        nbrOfInfractionWithPV = AEMIllegalFish.getNbrOfInfractionWithPV(fishActions),
        nbrOfTowedVessel = AEMIllegalFish.getNbrOfTowedVessel(fishActions),
        quantityOfFish = AEMIllegalFish.getQuantityOfFish(fishActions)
    ) {

    }

    companion object {
        fun getNbrOfHourInSea(fishActions: List<ExtendedFishActionEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfPolFishAction(fishActions: List<ExtendedFishActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfTargetedVessel(fishActions: List<ExtendedFishActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfInfraction(fishActions: List<ExtendedFishActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfInfractionWithPV(fishActions: List<ExtendedFishActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfTowedVessel(fishActions: List<ExtendedFishActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getQuantityOfFish(fishActions: List<ExtendedFishActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

    }
}
