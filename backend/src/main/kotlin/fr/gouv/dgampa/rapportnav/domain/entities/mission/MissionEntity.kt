package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Completion
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import org.locationtech.jts.geom.MultiPolygon
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
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
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val isClosed: Boolean,
    val isDeleted: Boolean,
    val isGeometryComputedFromControls: Boolean,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
    var actions: List<MissionActionEntity>? = null,
    val generalInfo: MissionGeneralInfoEntity? = null,
    val crew: List<MissionCrewEntity>? = null,
    var status: MissionStatusEnum? = null,
    var reportStatus: MissionReportStatusEntity? = null
) {

    private val logger = LoggerFactory.getLogger(MissionEntity::class.java)

    constructor(
        envMission: ExtendedEnvMissionEntity,
        navMission: NavMissionEntity? = null,
        fishMissionActions: List<ExtendedFishActionEntity>? = null
    ) : this(
        id = (envMission.mission.id)!!,
        missionTypes = envMission.mission.missionTypes,
        controlUnits = envMission.mission.controlUnits,
        openBy = envMission.mission.openBy,
        completedBy = envMission.mission.completedBy,
        observationsCacem = envMission.mission.observationsCacem,
        observationsCnsp = envMission.mission.observationsCnsp,
        facade = envMission.mission.facade,
        geom = envMission.mission.geom,
        startDateTimeUtc = envMission.mission.startDateTimeUtc,
        endDateTimeUtc = envMission.mission.endDateTimeUtc,
        isClosed = envMission.mission.isClosed,
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
    ) {
        this.status = this.calculateMissionStatus(
            isClosed = envMission.mission.isClosed,
            startDateTimeUtc = envMission.mission.startDateTimeUtc,
            endDateTimeUtc = envMission.mission.endDateTimeUtc,
        )
        this.reportStatus = this.calculateMissionReportStatus()
    }

    companion object {
        private fun sortActions(
            envActions: List<MissionActionEntity.EnvAction>,
            fishActions: List<MissionActionEntity.FishAction>,
            navActions: List<MissionActionEntity.NavAction>
        ): List<MissionActionEntity> {
            return (envActions + fishActions + navActions).sortedByDescending { action ->
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
    }

    private fun calculateMissionStatus(
        isClosed: Boolean,
        startDateTimeUtc: ZonedDateTime,
        endDateTimeUtc: ZonedDateTime? = null,
    ): MissionStatusEnum {
        val compareDate = ZonedDateTime.now()

        if (isClosed) {
            return MissionStatusEnum.ENDED
        }

        try {
            val startDateTime = ZonedDateTime.parse(startDateTimeUtc.toString())
            if (startDateTime.isAfter(compareDate) || startDateTime.isEqual(compareDate)) {
                return MissionStatusEnum.UPCOMING
            }
        } catch (e: DateTimeParseException) {
            logger.error("MissionEntity > calculateMissionStatus - error with startDateTime", e)
            return MissionStatusEnum.UNAVAILABLE
        }

        if (endDateTimeUtc != null) {
            try {
                val endDateTime = ZonedDateTime.parse(endDateTimeUtc.toString())
                if (endDateTime.isAfter(compareDate)) {
                    return MissionStatusEnum.IN_PROGRESS
                } else if (endDateTime.isBefore(compareDate) || endDateTime.isEqual(compareDate)) {
                    return MissionStatusEnum.ENDED
                }
            } catch (e: DateTimeParseException) {
                logger.error("MissionEntity > calculateMissionStatus - error with endDateTime", e)
                return MissionStatusEnum.UNAVAILABLE
            }
        }

        return MissionStatusEnum.PENDING
    }

    private fun calculateMissionReportStatus(): MissionReportStatusEntity {

        var status: MissionReportStatusEnum = MissionReportStatusEnum.COMPLETE
        val sources: MutableList<MissionSourceEnum> = mutableListOf()

        // check all actions
        this.actions?.forEach { action ->
            when {
                action is MissionActionEntity.NavAction && action.navAction.isCompleteForStats == false -> {
                    sources.add(MissionSourceEnum.RAPPORTNAV)
                    status = MissionReportStatusEnum.INCOMPLETE
                }

                action is MissionActionEntity.EnvAction -> {
                    if (action.envAction?.controlAction?.action?.completion == ActionCompletionEnum.TO_COMPLETE
                        ||
                        action.envAction?.surveillanceAction?.action?.completion == ActionCompletionEnum.TO_COMPLETE
                    ) {
                        sources.add(MissionSourceEnum.MONITORENV)
                        status = MissionReportStatusEnum.INCOMPLETE
                    }
                }

                action is MissionActionEntity.FishAction && action.fishAction.controlAction?.action?.completion == Completion.TO_COMPLETE -> {
                    sources.add(MissionSourceEnum.MONITORFISH)
                    status = MissionReportStatusEnum.INCOMPLETE
                }
            }
        }

        // check crew
        if (this.crew.isNullOrEmpty()) {
            status = MissionReportStatusEnum.INCOMPLETE
            sources.add(MissionSourceEnum.RAPPORTNAV)
        }

        // check general info
        if (this.generalInfo === null || (this.generalInfo.consumedFuelInLiters === null || this.generalInfo.consumedGOInLiters == null || this.generalInfo.distanceInNauticalMiles === null)) {
            status = MissionReportStatusEnum.INCOMPLETE
            sources.add(MissionSourceEnum.RAPPORTNAV)
        }

        return MissionReportStatusEntity(
            status = status,
            sources = sources.distinct()
        )

    }
}

