package fr.gouv.dgampa.rapportnav.domain.entities.aem.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils
import java.time.Instant

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
        fishActions: List<MissionFishActionEntity?>,
        navActions: List<MissionNavActionEntity>,
        missionEndDateTime: Instant?
    ) : this(
        nbrOfHourAtSea = getNbrOfHourAtSea(fishActions, navActions, missionEndDateTime),
        nbrOfPolFishAction = fishActions.size.toDouble(),
        nbrOfTargetedVessel = fishActions.size.toDouble(),
        nbrOfInfraction = getNbrOfInfraction(fishActions),
        nbrOfInfractionWithPV = getNbrOfInfractionWithPV(fishActions),
        nbrOfSeizureAndDiversionVessel = getNbrOfSeizureAndDiversionVessel(fishActions),
        quantityOfFish = getQuantityOfFish(fishActions)
    ) {
    }

    companion object {
        fun getNbrOfHourAtSea(fishActions: List<MissionFishActionEntity?>,
                              navActions: List<MissionNavActionEntity>,
                              missionEndDateTime: Instant?): Double {
            // changes on Oct 2025 : add nav and anchored durations on top of the amount of time in fish controls
            // equals to 3.4.1 + 7.4
            val navAndAnchored = AEMSovereignProtect2(navActions = navActions, envActions = emptyList(), fishActions = fishActions, missionEndDateTime = missionEndDateTime).nbrOfHourAtSea
            return navAndAnchored?.plus(fishActions.filter { it?.fishActionType === MissionActionType.LAND_CONTROL || it?.fishActionType === MissionActionType.SEA_CONTROL }.fold(0.0) { acc, fishAction ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = fishAction?.startDateTimeUtc,
                        endDateTimeUtc = fishAction?.endDateTimeUtc
                    )
                )
            }) ?: 0.0;
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
