package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.toMapForExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.ExportParams
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.EncodeSpecialChars
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter
import kotlin.time.DurationUnit

@UseCase
class ExportMission(
    private val exportRepository: IRpnExportRepository,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMission: GetMission,
    private val navActionStatus: INavActionStatusRepository,
    private val mapStatusDurations: MapStatusDurations,
    private val formatActionsForTimeline: FormatActionsForTimeline,
    private val getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus,
    private val computeDurations: ComputeDurations,
    private val getInfoAboutNavAction: GetInfoAboutNavAction,
    private val encodeSpecialChars: EncodeSpecialChars,
) {

    private val logger = LoggerFactory.getLogger(ExportMission::class.java)

    fun exportOdt(missionId: Int): MissionExportEntity? {
        try {
            val mission: MissionEntity? = getMission.execute(missionId = missionId)
            if (mission == null) {
                logger.error("[RapportDePatrouille] - Mission not found for missionId: $missionId")
                return null
            }

            val generalInfo: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId)
            val agentsCrew: List<MissionCrewEntity> =
                agentsCrewByMissionId.execute(missionId = missionId, commentDefaultsToString = true)
            val statuses = navActionStatus.findAllByMissionId(missionId = missionId).sortedBy { it.startDateTimeUtc }
                .map { it.toActionStatusEntity() }

            val durations = mapStatusDurations.execute(mission, statuses, DurationUnit.HOURS)
            val missionDuration = computeDurations.convertFromSeconds(
                computeDurations.durationInSeconds(
                    mission.startDateTimeUtc,
                    mission.endDateTimeUtc,

                    ) ?: 0,
                DurationUnit.HOURS
            )

            val nbOfDaysAtSea = getNbOfDaysAtSeaFromNavigationStatus.execute(
                missionStartDateTime = mission.startDateTimeUtc,
                missionEndDateTime = mission.endDateTimeUtc,
                actions = statuses,
                durationUnit = DurationUnit.HOURS
            )

            val timeline = formatActionsForTimeline.formatTimeline(mission.actions)

            val rescueInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.RESCUE),
                actionSource = MissionSourceEnum.RAPPORTNAV,
            )?.toMapForExport()
            val nauticalEventsInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.NAUTICAL_EVENT),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val antiPollutionInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.ANTI_POLLUTION),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val baaemAndVigimerInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.VIGIMER, ActionType.BAAEM_PERMANENCE),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val illegalImmigrationInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.ILLEGAL_IMMIGRATION),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val envSurveillanceInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.SURVEILLANCE),
                actionSource = MissionSourceEnum.MONITORENV
            )?.toMapForExport()

            val exportParams = ExportParams(
                service = mission.openBy,
                id = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(mission.startDateTimeUtc),
                startDateTime = mission.startDateTimeUtc,
                endDateTime = mission.endDateTimeUtc,
                observations = mission.observationsByUnit?.let { encodeSpecialChars.escapeForXML(it) } ?: "",
                presenceMer = durations["atSeaDurations"].orEmpty(),
                presenceQuai = durations["dockingDurations"].orEmpty(),
                indisponibilite = durations["unavailabilityDurations"].orEmpty(),
                nbJoursMer = nbOfDaysAtSea,
                dureeMission = missionDuration.toInt(),
                patrouilleEnv = 0,
                patrouilleMigrant = 0,
                distanceMilles = generalInfo?.distanceInNauticalMiles,
                goMarine = generalInfo?.consumedGOInLiters,
                essence = generalInfo?.consumedFuelInLiters,
                crew = agentsCrew,
                timeline = formatActionsForTimeline.formatForRapportNav1(timeline),
                rescueInfo = rescueInfo,
                nauticalEventsInfo = nauticalEventsInfo,
                antiPollutionInfo = antiPollutionInfo,
                baaemAndVigimerInfo = baaemAndVigimerInfo,
                patrouilleSurveillanceEnvInHours = envSurveillanceInfo?.get("durationInHours")?.toFloatOrNull(),
                patrouilleMigrantInHours = illegalImmigrationInfo?.get("durationInHours")?.toFloatOrNull()
            )

            return exportRepository.exportOdt(exportParams)
        } catch (e: Exception) {
            logger.error("[RapportDePatrouille] - Error building data before sending it to RapportNav1 : ${e.message}")
            return null
        }
    }

}
