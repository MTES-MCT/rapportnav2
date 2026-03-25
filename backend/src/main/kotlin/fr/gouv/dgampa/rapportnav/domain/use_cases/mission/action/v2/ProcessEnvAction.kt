package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates


@UseCase
class ProcessEnvAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeEnvTarget: GetComputeEnvTarget,
    private val getMissionDates: GetMissionDates
) : AbstractGetMissionAction(getStatusForAction) {

    /**
     * Processes an Env action by computing targets and validating for stats.
     * Automatically determines if mission is finished based on mission dates.
     *
     * @param missionId The mission ID
     * @param envAction The action to process
     */
    fun execute(missionId: Int, envAction: EnvActionEntity): MissionEnvActionEntity {
        val action = MissionEnvActionEntity.fromEnvAction(missionId = missionId, action = envAction)
        val targets = getComputeEnvTarget.execute(
            actionId = action.getActionId(),
            isControl = action.isControl(),
            envInfractions = action.envInfractions,
        )

        action.targets = targets
        action.status = this.getStatus(action)

        // Compute isMissionFinished internally
        val missionDates = getMissionDates.execute(missionId = missionId, ownerId = null)
        val isMissionFinished = missionDates?.isMissionFinished() ?: false

        action.computeValidity(isMissionFinished)
        return action
    }
}
