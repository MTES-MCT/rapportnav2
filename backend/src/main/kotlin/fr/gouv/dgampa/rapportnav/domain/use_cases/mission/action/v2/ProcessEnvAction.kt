package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan


@UseCase
class ProcessEnvAction(
    private val mapControlPlans: MapEnvActionControlPlans,
    getStatusForAction: GetStatusForAction,
    private val getComputeEnvTarget: GetComputeEnvTarget
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(missionId: Int, envAction: EnvActionEntity): MissionEnvActionEntity {
        val action = MissionEnvActionEntity.fromEnvAction(missionId = missionId, action = envAction)
        val targets = getComputeEnvTarget.execute(
            actionId = action.getActionId(),
            isControl = action.isControl(),
            envInfractions = action.envInfractions,
        )

        action.targets = targets
        action.status = this.getStatus(action)
        action.formattedControlPlans = getFormattedControlPlanList(action.controlPlans)
        action.computeCompleteness()
        return action
    }

    private fun getFormattedControlPlanList(controlPlans: List<EnvActionControlPlanEntity>?): List<FormattedEnvActionControlPlan>? {
        val filteredControlPlans: ControlPlansEntity? = mapControlPlans.execute(controlPlans)
        return FormattedEnvActionControlPlan.fromControlPlansEntity(filteredControlPlans)
    }
}
