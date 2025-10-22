package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.analytics

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.OperationalSummaryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.PatrolDataEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.GetMissionOperationalSummary2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.MapStatusDurations2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionActionControls

@UseCase
class ComputePatrolData(
    private val getComputeEnvMission: GetComputeEnvMission,
    private val mapStatusDurations2: MapStatusDurations2,
    private val getMissionOperationalSummary: GetMissionOperationalSummary2,
    private val getMissionActionControls: GetMissionActionControls,
) {
    fun execute(missionId: Int): PatrolDataEntity? {
        val mission: MissionEntity2? = getComputeEnvMission.execute(missionId = missionId)

        if (mission == null) {
            return null
        }

        // section "Activité du navire"
        val activity = computeActivity(mission = mission)

        // section "Bilan opérationnel"
        val operationalSummary = computeOperationalSummary(mission = mission)

        return PatrolDataEntity(
            id = missionId,
            idUUID = mission.generalInfos?.data?.missionIdUUID,
            serviceId = mission.generalInfos?.data?.serviceId,
            startDateTimeUtc = mission.data?.startDateTimeUtc,
            endDateTimeUtc = mission.data?.endDateTimeUtc,
            missionTypes = mission.data?.missionTypes,
            controlUnits = mission.data?.controlUnits,
            facade = mission.data?.facade,
            isDeleted = mission.data?.isDeleted,
            missionSource = mission.data?.missionSource,
            activity = activity,
//            operationalSummary = operationalSummary
        )
    }

    fun computeActivity(mission: MissionEntity2?): Map<String, Map<String, Double>> {
        val statuses = mission?.actions
            ?.filterIsInstance<MissionNavActionEntity>()
            ?.filter {it.actionType === ActionType.STATUS }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        val controls = getMissionActionControls.execute(mission)

        val activity: Map<String, Map<String, Double>> = mapStatusDurations2.execute(
            endDateTimeUtc = mission?.data?.endDateTimeUtc,
            statuses = statuses,
            controls = controls
        )
        return activity
    }

    fun computeOperationalSummary(mission: MissionEntity2?): OperationalSummaryEntity {

        val proFishingSeaSummary: LinkedHashMap<String, Map<String, Int?>>
            = getMissionOperationalSummary.getProFishingSeaSummary(mission?.actions.orEmpty())
        val proFishingLandSummary: LinkedHashMap<String, Map<String, Int?>>
            = getMissionOperationalSummary.getProFishingLandSummary(mission?.actions.orEmpty())

        val proSailingSeaSummary: Map<String, Int> = getMissionOperationalSummary.getProSailingSeaSummary(mission?.actions.orEmpty())
        val proSailingLandSummary: Map<String, Int> = getMissionOperationalSummary.getProSailingLandSummary(mission?.actions.orEmpty())

        val leisureSailingSeaSummary: Map<String, Int> = getMissionOperationalSummary.getLeisureSailingSeaSummary(mission?.actions.orEmpty())
        val leisureSailingLandSummary: Map<String, Int> = getMissionOperationalSummary.getLeisureSailingLandSummary(mission?.actions.orEmpty())

        val leisureFishingSummary: Map<String, Int> = getMissionOperationalSummary.getLeisureFishingSummary(mission?.actions.orEmpty())

        val envSummary: Map<String, Int> = getMissionOperationalSummary.getEnvSummary(mission?.actions.orEmpty())

        return OperationalSummaryEntity(
            proFishingSeaSummary = proFishingSeaSummary,
            proFishingLandSummary = proFishingLandSummary,
            proSailingSeaSummary = proSailingSeaSummary,
            proSailingLandSummary = proSailingLandSummary,
            leisureSailingSeaSummary = leisureSailingSeaSummary,
            leisureSailingLandSummary = leisureSailingLandSummary,
            leisureFishingSummary = leisureFishingSummary,
            envSummary = envSummary,
        )
    }

}
