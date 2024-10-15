package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

data class AEMIllegalFish(
    val nbrOfHourAtSea: Double? = 0.0, //4.3.1
    val nbrOfPolFishAction: Double? = 0.0, // 4.3.3
    val nbrOfTargetedVessel: Double? = 0.0, // 4.3.5
    val nbrOfInfractionWithPV: Double? = 0.0, // 4.3.6
    val nbrOfInfraction: Double? = 0.0, // 4.3.7
    val nbrOfSeizureAndDiversionVessel: Double? = 0.0, // 4.3.8
    val quantityOfFish: Double? = 0.0 //4.3.9
) {
    constructor(
        fishActions: List<ExtendedFishActionEntity?>
    ) : this(
        nbrOfHourAtSea = getNbrOfHourAtSea(fishActions),
        nbrOfPolFishAction = fishActions.size.toDouble(),
        nbrOfTargetedVessel = fishActions.size.toDouble(),
        nbrOfInfraction = getNbrOfInfraction(fishActions),
        nbrOfInfractionWithPV = getNbrOfInfractionWithPV(fishActions),
        nbrOfSeizureAndDiversionVessel = getNbrOfSeizureAndDiversionVessel(fishActions),
        quantityOfFish = getQuantityOfFish(fishActions)
    ) {
    }

    companion object {
        fun getNbrOfHourAtSea(fishActions: List<ExtendedFishActionEntity?>): Double {
            return fishActions.fold(0.0) { acc, fishAction ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = fishAction?.controlAction?.action?.actionDatetimeUtc,
                        endDateTimeUtc = fishAction?.controlAction?.action?.actionEndDatetimeUtc
                    )
                )
            };
        }

        fun getNbrOfInfraction(fishActions: List<ExtendedFishActionEntity?>): Double {
            return fishActions.map { it?.controlAction?.action }.fold(0.0) { acc, c ->
                acc.plus(c?.gearInfractions?.fold(0) { a, n -> a.plus(n.natinf ?: 0) } ?: 0)
                    .plus(c?.otherInfractions?.fold(0) { a, n -> a.plus(n.natinf ?: 0) } ?: 0)
                    .plus(c?.speciesInfractions?.fold(0) { a, n -> a.plus(n.natinf ?: 0) } ?: 0)
                    .plus(c?.logbookInfractions?.fold(0) { a, n -> a.plus(n.natinf ?: 0) } ?: 0)
            };
        }

        fun getNbrOfInfractionWithPV(fishActions: List<ExtendedFishActionEntity?>): Double {
            return fishActions.map { it?.controlAction?.action }.fold(0.0) { acc, c ->
                acc.plus(c?.gearInfractions?.filter { g -> g.infractionType == InfractionType.WITH_RECORD }?.size ?: 0)
                    .plus(
                        c?.otherInfractions?.filter { o -> o.infractionType == InfractionType.WITH_RECORD }?.size ?: 0
                    )
                    .plus(
                        c?.speciesInfractions?.filter { s -> s.infractionType == InfractionType.WITH_RECORD }?.size ?: 0
                    )
                    .plus(
                        c?.logbookInfractions?.filter { l -> l.infractionType == InfractionType.WITH_RECORD }?.size ?: 0
                    )
            };
        }

        fun getNbrOfSeizureAndDiversionVessel(fishActions: List<ExtendedFishActionEntity?>): Double {
            return fishActions.filter { it?.controlAction?.action?.seizureAndDiversion == true }.size.toDouble();
        }

        fun getQuantityOfFish(fishActions: List<ExtendedFishActionEntity?>): Double {
            return 0.0; //TODO: hasSomeSpeciesSeized
        }

    }
}
