package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates


@UseCase
class ProcessFishAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget,
    private val getMissionDates: GetMissionDates
) : AbstractGetMissionAction(getStatusForAction) {

    /**
     * Processes a Fish action by computing targets and validating for stats.
     * Automatically determines if mission is finished based on mission dates.
     *
     * @param missionId The mission ID
     * @param action The action to process
     */
    fun execute(missionId: Int, action: MissionAction): MissionFishActionEntity {
        val entity = MissionFishActionEntity.fromFishAction(action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())

        entity.targets = targets
        entity.status = this.getStatus(entity)

        // Compute isMissionFinished internally
        val missionDates = getMissionDates.execute(missionId = missionId, ownerId = null)
        val isMissionFinished = missionDates?.isMissionFinished() ?: false

        // compute validity
        entity.computeValidity(isMissionFinished)
        return entity
    }
}
