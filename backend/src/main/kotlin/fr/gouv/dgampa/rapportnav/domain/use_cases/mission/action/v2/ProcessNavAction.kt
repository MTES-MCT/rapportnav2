package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicy

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
     * @param policy the pre-resolved validation policy. Callers processing a whole mission resolve it once
     * and pass it in to avoid an N+1 mission-dates lookup. When null, it is resolved from this action's own
     * mission dates (required for inquiry actions, whose dates come from their inquiry rather than the mission).
     */
    fun execute(
        action: MissionNavActionEntity,
        bypassValidation: Boolean = false,
        policy: ValidationPolicy? = null
    ): MissionNavActionEntity {
        action.targets = getComputeTarget.execute(actionId = action.getActionId(), isControl = action.isControl())

        if (bypassValidation) {
            action.markCompleteForStats()
        } else {
            val resolvedPolicy = policy ?: ValidationPolicies.forMissionStartDate(
                getMissionDates.execute(
                    missionId = action.missionId,
                    ownerId = action.ownerId,
                    inquiryId = if (action.actionType == ActionType.INQUIRY) action.ownerId else null
                )?.startDateTimeUtc
            )
            action.computeValidity(validator = entityValidityValidator, policy = resolvedPolicy)
        }

        return action
    }
}
