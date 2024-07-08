package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity

data class AEMVesselRescue(
    val nbrOfHourInSea: Int, // 2.1
    val nbrOfRescuedOperation: Int?,// 2.3
    val nbrOfNoticedVessel: Int?, // 2.4
    val nbrOfTowedVessel: Int?, //2.5
) {
    constructor(
        actionRescues: List<ActionRescueEntity?>
    ) : this(
        nbrOfHourInSea = AEMVesselRescue.getNbrOfHourInSea(actionRescues),
        nbrOfRescuedOperation = actionRescues.size,
        nbrOfNoticedVessel = AEMVesselRescue.getNbrOfNoticedVessel(actionRescues),
        nbrOfTowedVessel = AEMVesselRescue.getNbrOfNoticedVessel(actionRescues)
    ) {
    }
    companion object {
        fun getNbrOfHourInSea(actionRescues: List<ActionRescueEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfNoticedVessel(actionRescues: List<ActionRescueEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.isVesselNoticed
            return 0;
        }

        fun getNbrOfTowedVessel(actionRescues: List<ActionRescueEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.isVesselTowed
            return 0;
        }
    }
}
