package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates

@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget,
    private val getMissionDates: GetMissionDates
) : AbstractGetMissionAction(getStatusForAction) {

    /**
     * Processes a Nav action by computing targets and validating for stats.
     * Automatically determines if mission is finished based on mission dates.
     *
     * @param action The action to process
     */
    fun execute(action: MissionNavActionEntity): MissionNavActionEntity {
        action.targets = getComputeTarget.execute(actionId = action.getActionId(), isControl = action.isControl())

        // Compute isMissionFinished internally
        val missionDates = getMissionDates.execute(missionId = action.missionId, ownerId = action.ownerId)
        val isMissionFinished = missionDates?.isMissionFinished() ?: false

        // compute validity
        action.computeValidity(isMissionFinished)
        return action
    }
}
