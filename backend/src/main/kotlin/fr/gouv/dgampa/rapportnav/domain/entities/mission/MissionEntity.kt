package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import org.locationtech.jts.geom.MultiPolygon
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.format.DateTimeParseException

data class MissionEntity(
    val id: Int,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val isDeleted: Boolean,
    val isGeometryComputedFromControls: Boolean,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
    var actions: List<MissionActionEntity>? = null,
    val generalInfo: MissionGeneralInfoEntity? = null,
    val crew: List<MissionCrewEntity>? = null,
    var status: MissionStatusEnum? = null,
    var completenessForStats: CompletenessForStatsEntity? = null,
    var services: List<ServiceEntity>? = null,
    @MandatoryForStats
    var observationsByUnit: String? = null
) {

    private val logger = LoggerFactory.getLogger(MissionEntity::class.java)

    constructor(
        envMission: ExtendedEnvMissionEntity,
        navMission: NavMissionEntity? = null,
        fishMissionActions: List<ExtendedFishActionEntity>? = null,
    ) : this(
        id = (envMission.mission.id)!!,
        missionTypes = envMission.mission.missionTypes ?: listOf(),
        controlUnits = envMission.mission.controlUnits,
        openBy = envMission.mission.openBy,
        completedBy = envMission.mission.completedBy,
        observationsCacem = envMission.mission.observationsCacem,
        observationsCnsp = envMission.mission.observationsCnsp,
        facade = envMission.mission.facade,
        geom = envMission.mission.geom,
        startDateTimeUtc = envMission.mission.startDateTimeUtc,
        endDateTimeUtc = envMission.mission.endDateTimeUtc,
        isDeleted = envMission.mission.isDeleted,
        isGeometryComputedFromControls = envMission.mission.isGeometryComputedFromControls,
        missionSource = envMission.mission.missionSource,
        hasMissionOrder = envMission.mission.hasMissionOrder,
        isUnderJdp = envMission.mission.isUnderJdp,
        actions = sortActions(
            envMission.actions?.map { MissionActionEntity.EnvAction(it) } ?: emptyList(),
            fishMissionActions?.map { MissionActionEntity.FishAction(it) } ?: emptyList(),
            navMission?.actions?.map { MissionActionEntity.NavAction(it) } ?: emptyList()
        ),
        generalInfo = navMission?.generalInfo,
        crew = navMission?.crew,
        services = navMission?.services,
        observationsByUnit = envMission.mission.observationsByUnit
    ) {
        this.status = this.calculateMissionStatus(
            startDateTimeUtc = envMission.mission.startDateTimeUtc,
            endDateTimeUtc = envMission.mission.endDateTimeUtc,
        )
        this.completenessForStats = this.calculateCompletenessForStats()
    }

    companion object {

        fun sortActionsAsc(actions: List<MissionActionEntity>): List<MissionActionEntity> {
            return (actions).sortedByDescending { action ->
                when (action) {
                    is MissionActionEntity.EnvAction -> {
                        if (action.envAction?.controlAction !== null) {
                            action.envAction.controlAction.action?.actionStartDateTimeUtc
                        } else {
                            action.envAction?.surveillanceAction?.action?.actionStartDateTimeUtc
                        }
                    }

                    is MissionActionEntity.FishAction -> action.fishAction.controlAction?.action?.actionDatetimeUtc
                    is MissionActionEntity.NavAction -> action.navAction.startDateTimeUtc
                }
            }
        }

        fun sortActions(
            envActions: List<MissionActionEntity.EnvAction>,
            fishActions: List<MissionActionEntity.FishAction>,
            navActions: List<MissionActionEntity.NavAction>
        ): List<MissionActionEntity> {
            return sortActionsAsc((envActions + fishActions + navActions))
        }

        fun sortActions(
            actions: List<MissionActionEntity>,
        ): List<MissionActionEntity> {
            return sortActionsAsc(actions)
        }
    }


    private fun calculateMissionStatus(
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
    ): MissionStatusEnum {
        val compareDate = Instant.now()

        if (endDateTimeUtc == null) {
            return MissionStatusEnum.UNAVAILABLE
        }

        if (endDateTimeUtc != null && Instant.parse(endDateTimeUtc.toString()).isBefore(compareDate)) {
            return MissionStatusEnum.ENDED
        }

        try {
            val startDateTime = Instant.parse(startDateTimeUtc.toString())
            if (startDateTime.isAfter(compareDate) || startDateTime.equals(compareDate)) {
                return MissionStatusEnum.UPCOMING
            }
        } catch (e: DateTimeParseException) {
            logger.error("MissionEntity > calculateMissionStatus - error with startDateTime", e)
            return MissionStatusEnum.UNAVAILABLE
        }

        if (endDateTimeUtc != null) {
            try {
                val endDateTime = Instant.parse(endDateTimeUtc.toString())
                if (endDateTime.isAfter(compareDate)) {
                    return MissionStatusEnum.IN_PROGRESS
                } else if (endDateTime.isBefore(compareDate) || endDateTime.equals(compareDate)) {
                    return MissionStatusEnum.ENDED
                }
            } catch (e: DateTimeParseException) {
                logger.error("MissionEntity > calculateMissionStatus - error with endDateTime", e)
                return MissionStatusEnum.UNAVAILABLE
            }
        }

        return MissionStatusEnum.UNAVAILABLE
    }

    private fun calculateCompletenessForStats(): CompletenessForStatsEntity {

        var status: CompletenessForStatsStatusEnum = CompletenessForStatsStatusEnum.COMPLETE
        val sources: MutableList<MissionSourceEnum> = mutableListOf()

        // check all actions
        this.actions?.forEach { action ->
            when {
                action is MissionActionEntity.NavAction && action.navAction.isCompleteForStats == false -> {
                    sources.add(MissionSourceEnum.RAPPORTNAV)
                    status = CompletenessForStatsStatusEnum.INCOMPLETE
                }

                action is MissionActionEntity.EnvAction -> {
                    if (action.envAction?.controlAction != null && action.envAction.controlAction.isCompleteForStats != true) {
                        action.envAction.controlAction.sourcesOfMissingDataForStats?.map {
                            sources.add(it)
                        }
                        status = CompletenessForStatsStatusEnum.INCOMPLETE
                    }
                    if (action.envAction?.surveillanceAction != null && action.envAction.surveillanceAction.isCompleteForStats != true) {
                        action.envAction.surveillanceAction.sourcesOfMissingDataForStats?.map {
                            sources.add(it)
                        }
                        status = CompletenessForStatsStatusEnum.INCOMPLETE
                    }
                }

                action is MissionActionEntity.FishAction && (action.fishAction.controlAction?.isCompleteForStats != true) -> {
                    action.fishAction.controlAction?.sourcesOfMissingDataForStats?.map {
                        sources.add(it)
                    }
                    status = CompletenessForStatsStatusEnum.INCOMPLETE
                }
            }
        }

        // check crew
        if (this.crew.isNullOrEmpty()) {
            status = CompletenessForStatsStatusEnum.INCOMPLETE
            sources.add(MissionSourceEnum.RAPPORTNAV)
        }

        // check general info
        if (this.generalInfo === null || (this.generalInfo.consumedFuelInLiters === null || this.generalInfo.consumedGOInLiters === null || this.generalInfo.distanceInNauticalMiles === null)) {
            status = CompletenessForStatsStatusEnum.INCOMPLETE
            sources.add(MissionSourceEnum.RAPPORTNAV)
        }

        return CompletenessForStatsEntity(
            status = status,
            sources = sources.distinct()
        )

    }
}

