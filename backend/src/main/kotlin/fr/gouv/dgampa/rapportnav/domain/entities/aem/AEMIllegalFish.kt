package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

data class AEMIllegalFish(
    val nbrOfHourAtSea: Int? = 0, //4.3.1
    val nbrOfPolFishAction: Int? = 0, // 4.3.3
    val nbrOfTargetedVessel: Int? = 0, // 4.3.4
    val nbrOfInfraction: Int? = 0, // 4.3.5
    val nbrOfInfractionWithPV: Int? = 0, // 4.3.6
    val nbrOfSeizureAndDiversionVessel: Int? = 0, // 4.3.8
    val quantityOfFish: Int? = 0 //4.3.9
) {
    constructor(
        fishActions: List<ExtendedFishActionEntity?>
    ) : this(
        nbrOfHourAtSea = getNbrOfHourAtSea(fishActions),
        nbrOfPolFishAction = fishActions.size,
        nbrOfTargetedVessel = fishActions.size,
        nbrOfInfraction = getNbrOfInfraction(fishActions),
        nbrOfInfractionWithPV = getNbrOfInfractionWithPV(fishActions),
        nbrOfSeizureAndDiversionVessel = getNbrOfSeizureAndDiversionVessel(fishActions),
        quantityOfFish = getQuantityOfFish(fishActions)
    ) {
    }

    companion object {
        fun getNbrOfHourAtSea(fishActions: List<ExtendedFishActionEntity?>): Int {
            return fishActions.fold(0.0) { acc, fishAction ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = fishAction?.controlAction?.action?.actionDatetimeUtc,
                        endDateTimeUtc = fishAction?.controlAction?.action?.actionEndDatetimeUtc
                    )
                )
            }.toInt();
        }

        fun getNbrOfInfraction(fishActions: List<ExtendedFishActionEntity?>): Int {
            return fishActions.map { it?.controlAction?.action }.fold(0) { acc, c ->
                acc.plus(c!!.gearInfractions.size)
                    .plus(c.otherInfractions.size)
                    .plus(c.speciesInfractions.size)
                    .plus(c.logbookInfractions.size)
            };
        }

        fun getNbrOfInfractionWithPV(fishActions: List<ExtendedFishActionEntity?>): Int {
            return fishActions.map { it?.controlAction?.action }.fold(0) { acc, c ->
                acc.plus(c!!.gearInfractions.filter { g -> g.infractionType == InfractionType.WITH_RECORD }.size)
                    .plus(c.otherInfractions.filter { o -> o.infractionType == InfractionType.WITH_RECORD }.size)
                    .plus(c.speciesInfractions.filter { s -> s.infractionType == InfractionType.WITH_RECORD }.size)
                    .plus(c.logbookInfractions.filter { l -> l.infractionType == InfractionType.WITH_RECORD }.size)
            };
        }

        fun getNbrOfSeizureAndDiversionVessel(fishActions: List<ExtendedFishActionEntity?>): Int {
            return fishActions.filter { it?.controlAction?.action?.seizureAndDiversion == true }.size;
        }

        fun getQuantityOfFish(fishActions: List<ExtendedFishActionEntity?>): Int {
            return 0;
        }

    }
}
