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
import org.slf4j.LoggerFactory

@UseCase
class ExportMission(
    private val exportRepository: IRpnExportRepository,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMissionById: GetMissionById,
    private val navActionStatus: INavActionStatusRepository,
    private val mapStatusDurations: MapStatusDurations,
    private val formatActionsForTimeline: FormatActionsForTimeline,
) {

    private val logger = LoggerFactory.getLogger(ExportMission::class.java)

    fun exportOdt(missionId: Int): MissionExportEntity? {
        try {
            val mission: MissionEntity? = getMissionById.execute(missionId = missionId)
            if (mission == null) {
                logger.error("[exportOdt] Mission not found for missionId: $missionId")
                return null
            }

            val generalInfo: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId)
            val agentsCrew: List<MissionCrewEntity> = agentsCrewByMissionId.execute(missionId = missionId)
            val statuses = navActionStatus.findAllByMissionId(missionId = missionId).sortedBy { it.startDateTimeUtc }
                .map { it.toActionStatusEntity() }

            val durations = mapStatusDurations.execute(mission, statuses)
            val missionDuration = (durations["atSeaDurations"]?.get("total") ?: 0) +
                (durations["dockingDurations"]?.get("total") ?: 0) +
                (durations["unavailabilityDurations"]?.get("total") ?: 0)

            val timeline = formatActionsForTimeline.formatTimeline(mission.actions)

            val exportParams = ExportParams(
                service = mission.openBy,
                id = "pam" + mission.id,
                startDateTime = mission.startDateTimeUtc,
                endDateTime = mission.endDateTimeUtc,
                presenceMer = durations["atSeaDurations"].orEmpty(),
                presenceQuai = durations["dockingDurations"].orEmpty(),
                indisponibilite = durations["unavailabilityDurations"].orEmpty(),
                nbJoursMer = 0,
                dureeMission = missionDuration,
                patrouilleEnv = 0,
                patrouilleMigrant = 0,
                distanceMilles = generalInfo?.distanceInNauticalMiles,
                goMarine = generalInfo?.consumedGOInLiters,
                essence = generalInfo?.consumedFuelInLiters,
                crew = agentsCrew,
                timeline = timeline
            )

            return exportRepository.exportOdt(exportParams)
        } catch (e: Exception) {
            logger.error("[exportOdt] error occurred during exportOdt: ${e.message}")
            return null
        }
    }

}