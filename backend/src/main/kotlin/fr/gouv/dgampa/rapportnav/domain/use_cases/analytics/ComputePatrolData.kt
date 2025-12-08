package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.PatrolDataEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.controlPolicies.ComputeControlPolicies
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.operationalSummary.ComputeAllOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.MapStatusDurations2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionActionControls

@UseCase
class ComputePatrolData(
    private val getComputeEnvMission: GetComputeEnvMission,
    private val mapStatusDurations2: MapStatusDurations2,
    private val computeAllOperationalSummary: ComputeAllOperationalSummary,
    private val getMissionActionControls: GetMissionActionControls,
    private val computeControlPolicies: ComputeControlPolicies,
) {
    fun execute(missionId: Int): PatrolDataEntity? {
        val mission: MissionEntity2? = getComputeEnvMission.execute(missionId = missionId)

        if (mission == null) {
            return null
        }

        // section "Activité du navire"
        val activity = computeActivity(mission = mission)

        // section "Bilan opérationnel"
        val operationalSummary = computeAllOperationalSummary.execute(mission = mission)

        //  section "Politiques publiques de contrôles"
        val controlPolicies = computeControlPolicies.execute(mission = mission)

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
            activity = activity,
            operationalSummary = operationalSummary,
            controlPolicies = controlPolicies
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
            startDateTimeUtc = mission?.data?.startDateTimeUtc,
            endDateTimeUtc = mission?.data?.endDateTimeUtc,
            statuses = statuses,
            controls = controls
        )
        return activity
    }


}
