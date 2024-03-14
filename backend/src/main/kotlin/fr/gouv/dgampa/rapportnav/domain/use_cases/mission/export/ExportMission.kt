package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.ExportParams
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter
import kotlin.time.DurationUnit

@UseCase
class ExportMission(
    private val exportRepository: IRpnExportRepository,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionById: GetMissionById,
    private val navActionStatus: INavActionStatusRepository,
    private val mapStatusDurations: MapStatusDurations,
    private val formatActionsForTimeline: FormatActionsForTimeline,
    private val getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus,
) {

    private val logger = LoggerFactory.getLogger(ExportMission::class.java)

    fun exportOdt(missionId: Int): MissionExportEntity? {
        try {
            val mission: MissionEntity? = getMissionById.execute(missionId = missionId)
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
            val missionDuration = (durations["atSeaDurations"]?.get("total") ?: 0) +
                (durations["dockingDurations"]?.get("total") ?: 0) +
                (durations["unavailabilityDurations"]?.get("total") ?: 0)

            val nbOfDaysAtSea = getNbOfDaysAtSeaFromNavigationStatus.execute(
                missionStartDateTime = mission.startDateTimeUtc,
                missionEndDateTime = mission.endDateTimeUtc,
                actions = statuses,
                durationUnit = DurationUnit.HOURS
            )

            val timeline = formatActionsForTimeline.formatTimeline(mission.actions)

            val exportParams = ExportParams(
                service = mission.openBy,
                id = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(mission.startDateTimeUtc),
                startDateTime = mission.startDateTimeUtc,
                endDateTime = mission.endDateTimeUtc,
                presenceMer = durations["atSeaDurations"].orEmpty(),
                presenceQuai = durations["dockingDurations"].orEmpty(),
                indisponibilite = durations["unavailabilityDurations"].orEmpty(),
                nbJoursMer = nbOfDaysAtSea,
                dureeMission = missionDuration,
                patrouilleEnv = 0,
                patrouilleMigrant = 0,
                distanceMilles = generalInfo?.distanceInNauticalMiles,
                goMarine = generalInfo?.consumedGOInLiters,
                essence = generalInfo?.consumedFuelInLiters,
                crew = agentsCrew,
                timeline = formatActionsForTimeline.formatForRapportNav1(timeline)
            )

            return exportRepository.exportOdt(exportParams)
        } catch (e: Exception) {
            logger.error("[RapportDePatrouille] - Error building data before sending it to RapportNav1: ${e.message}")
            return null
        }
    }

}
