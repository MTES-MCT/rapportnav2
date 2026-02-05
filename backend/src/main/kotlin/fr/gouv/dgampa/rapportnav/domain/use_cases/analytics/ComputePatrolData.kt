package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.PatrolDataEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.NavActionInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.ComputeInternTrainingSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies.ComputeControlPolicies
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeAllOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetInfoAboutNavAction2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.MapStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionActionControls

@UseCase
class ComputePatrolData(
    private val getComputeEnvMission: GetComputeEnvMission,
    private val mapStatusDurations: MapStatusDurations,
    private val computeAllOperationalSummary: ComputeAllOperationalSummary,
    private val getMissionActionControls: GetMissionActionControls,
    private val computeControlPolicies: ComputeControlPolicies,
    private val getInfoAboutNavAction2: GetInfoAboutNavAction2,
    private val computeInternTrainingSummary: ComputeInternTrainingSummary,
) {
    fun execute(missionId: Int): PatrolDataEntity? {
        val mission: MissionEntity? = getComputeEnvMission.execute(missionId = missionId)

        if (mission == null) {
            return null
        }

        // section "Activité du navire"
        val activity = computeActivity(mission = mission)

        // section "Bilan opérationnel"
        val operationalSummary = computeAllOperationalSummary.execute(mission = mission)

        //  section "Politiques publiques de contrôles"
        val controlPolicies = computeControlPolicies.execute(mission = mission)

        // section "Autres missions"
        val otherActionsSummary = computeOtherActionsSummary(mission = mission)

        // section "Soutien à la politique de formation"
        val internTrainingSummary = computeInternTrainingSummary.execute(passengers = mission.generalInfos?.passengers)

        return PatrolDataEntity(
            id = missionId,
            idUUID = mission.generalInfos?.data?.missionIdUUID,
            serviceId = mission.generalInfos?.data?.service?.id,
            startDateTimeUtc = mission.data?.startDateTimeUtc,
            endDateTimeUtc = mission.data?.endDateTimeUtc,
            missionTypes = mission.data?.missionTypes,
            controlUnits = mission.data?.controlUnits,
            facade = mission.data?.facade,
            isDeleted = mission.data?.isDeleted,
            missionSource = mission.data?.missionSource,
            generalInfos = mission.generalInfos,
            activity = activity,
            operationalSummary = operationalSummary,
            controlPolicies = controlPolicies,
            otherActionsSummary = otherActionsSummary,
            internTrainingSummary = internTrainingSummary
        )
    }

    fun computeActivity(mission: MissionEntity?): Map<String, Map<String, Double>> {
        val statuses = mission?.actions
            ?.filterIsInstance<MissionNavActionEntity>()
            ?.filter {it.actionType === ActionType.STATUS }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        val controls = getMissionActionControls.execute(mission)

        val activity: Map<String, Map<String, Double>> = mapStatusDurations.execute(
            startDateTimeUtc = mission?.data?.startDateTimeUtc,
            endDateTimeUtc = mission?.data?.endDateTimeUtc,
            statuses = statuses,
            controls = controls
        )
        return activity
    }

    fun computeOtherActionsSummary(mission: MissionEntity?): Map<String, NavActionInfoEntity?> {

        val rescueInfo = getInfoAboutNavAction2.execute(
            actions = mission?.actions,
            actionTypes = listOf(ActionType.RESCUE),
            actionSource = MissionSourceEnum.RAPPORTNAV,
        )
        val nauticalEventsInfo = getInfoAboutNavAction2.execute(
            actions = mission?.actions,
            actionTypes = listOf(ActionType.NAUTICAL_EVENT),
            actionSource = MissionSourceEnum.RAPPORTNAV
        )
        val antiPollutionInfo = getInfoAboutNavAction2.execute(
            actions = mission?.actions,
            actionTypes = listOf(ActionType.ANTI_POLLUTION),
            actionSource = MissionSourceEnum.RAPPORTNAV
        )
        val baaemAndVigimerInfo = getInfoAboutNavAction2.execute(
            actions = mission?.actions,
            actionTypes = listOf(ActionType.VIGIMER, ActionType.BAAEM_PERMANENCE),
            actionSource = MissionSourceEnum.RAPPORTNAV
        )
        val illegalImmigrationInfo = getInfoAboutNavAction2.execute(
            actions = mission?.actions,
            actionTypes = listOf(ActionType.ILLEGAL_IMMIGRATION),
            actionSource = MissionSourceEnum.RAPPORTNAV
        )
        val envSurveillanceInfo = getInfoAboutNavAction2.execute(
            actions = mission?.actions,
            actionTypes = listOf(ActionType.SURVEILLANCE),
            actionSource = MissionSourceEnum.MONITORENV
        )

        return mapOf(
            "rescue" to rescueInfo,
            "nauticalEvents" to nauticalEventsInfo,
            "antiPollution" to antiPollutionInfo,
            "baaemAndVigimer" to baaemAndVigimerInfo,
            "illegalImmigration" to illegalImmigrationInfo,
            "envSurveillance" to envSurveillanceInfo,
        )
    }


}
