package fr.gouv.dgampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

data class AEMIllegalFish2(
    val nbrOfHourAtSea: Double? = 0.0, //4.3.1
    val nbrOfPolFishAction: Double? = 0.0, // 4.3.3
    val nbrOfTargetedVessel: Double? = 0.0, // 4.3.5
    val nbrOfInfractionWithPV: Double? = 0.0, // 4.3.6
    val nbrOfInfraction: Double? = 0.0, // 4.3.7
    val nbrOfSeizureAndDiversionVessel: Double? = 0.0, // 4.3.8
    val quantityOfFish: Double? = 0.0 //4.3.9
) {
    constructor(
        fishActions: List<MissionFishActionEntity?>
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
        fun getNbrOfHourAtSea(fishActions: List<MissionFishActionEntity?>): Double {
            return fishActions.fold(0.0) { acc, fishAction ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = fishAction?.startDateTimeUtc,
                        endDateTimeUtc = fishAction?.endDateTimeUtc
                    )
                )
            };
        }

        fun getNbrOfInfraction(fishActions: List<MissionFishActionEntity?>): Double {
            return fishActions.filterNotNull().fold(0.0) { acc, c ->
                acc.plus(c.gearInfractions?.count { it.natinf != null } ?: 0)
                    .plus(c.otherInfractions?.count { it.natinf != null } ?: 0)
                    .plus(c.speciesInfractions?.count { it.natinf != null } ?: 0)
                    .plus(c.logbookInfractions?.count { it.natinf != null } ?: 0)
            };
        }

        fun getNbrOfInfractionWithPV(fishActions: List<MissionFishActionEntity?>): Double {
            return fishActions.filterNotNull()
                .fold(0.0) { acc, c ->
                acc.plus(c.gearInfractions?.filter { g -> g.infractionType == InfractionType.WITH_RECORD }?.size ?: 0)
                    .plus(
                        c.otherInfractions?.filter { o -> o.infractionType == InfractionType.WITH_RECORD }?.size ?: 0
                    )
                    .plus(
                        c.speciesInfractions?.filter { s -> s.infractionType == InfractionType.WITH_RECORD }?.size ?: 0
                    )
                    .plus(
                        c.logbookInfractions?.filter { l -> l.infractionType == InfractionType.WITH_RECORD }?.size ?: 0
                    )
            };
        }

        fun getNbrOfSeizureAndDiversionVessel(fishActions: List<MissionFishActionEntity?>): Double {
            return fishActions.filter { it?.seizureAndDiversion == true }.size.toDouble();
        }

        fun getQuantityOfFish(fishActions: List<MissionFishActionEntity?>): Double {
            return fishActions
                .filterNotNull()
                .sumOf { it.speciesQuantitySeized?.toDouble() ?: 0.0 } }

    }
}
