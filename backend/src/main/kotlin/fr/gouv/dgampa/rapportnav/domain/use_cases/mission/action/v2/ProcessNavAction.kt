package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies

@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget,
    private val getMissionDates: GetMissionDates,
    private val entityValidityValidator: EntityValidityValidator
) : AbstractGetMissionAction(getStatusForAction) {

    /**
     * @param bypassValidation when true, skip the per-field completeness computation and mark the action
     * valid (the parent mission is already known complete — see [MissionActionEntity.markCompleteForStats]).
     * When false, run the full validation.
     */
    fun execute(action: MissionNavActionEntity, bypassValidation: Boolean = false): MissionNavActionEntity {
        action.targets = getComputeTarget.execute(actionId = action.getActionId(), isControl = action.isControl())

        if (bypassValidation) {
            action.markCompleteForStats()
        } else {
            val missionDates = getMissionDates.execute(
                missionId = action.missionId,
                ownerId = action.ownerId,
                inquiryId = if (action.actionType == ActionType.INQUIRY) action.ownerId else null
            )
            val policy = ValidationPolicies.forMissionStartDate(missionDates?.startDateTimeUtc)
            action.computeValidity(validator = entityValidityValidator, policy = policy)
        }

        return action
    }
}
