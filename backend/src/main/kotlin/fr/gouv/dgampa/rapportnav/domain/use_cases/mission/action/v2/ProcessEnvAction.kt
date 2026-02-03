package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction


@UseCase
class ProcessEnvAction(
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
        action.computeCompleteness()
        return action
    }

}
