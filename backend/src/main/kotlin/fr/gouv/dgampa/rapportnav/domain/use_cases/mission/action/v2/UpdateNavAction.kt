package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.ResolveActionOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData

@UseCase
class UpdateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val entityValidityValidator: EntityValidityValidator,
    private val computeActionValidityAndRecomputeMission: ComputeActionValidityAndRecomputeMission,
    private val resolveActionOwnerId: ResolveActionOwnerId
) {
    fun execute(id: String, input: MissionNavAction, ownerId: String? = null): MissionNavActionEntity {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        if (id != input.id) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "UpdateNavAction: action id mismatch"
            )
        }

        // Stamp the owner from the authoritative URL owner so an update never clears/omits ownerId.
        resolveActionOwnerId.execute(ownerId)?.let { action.ownerId = it }

        entityValidityValidator.validateAndThrow(action, ValidateThrowsBeforeSave::class.java)

        // Process targets first so completeness (which depends on controlsToComplete) is up to date.
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )

        // Compute the action's completeness BEFORE saving, since the nav row carries isCompleteForStats.
        val inquiryId = if (action.actionType == ActionType.INQUIRY) action.ownerId else null
        computeActionValidityAndRecomputeMission.computeActionValidity(
            action = action,
            ownerId = action.ownerId,
            inquiryId = inquiryId
        )

        missionActionRepository.save(action.toMissionActionModel())

        // Recompute the mission AFTER the save, so the aggregate re-reads the now-persisted action.
        computeActionValidityAndRecomputeMission.recomputeMission(action = action, ownerId = action.ownerId)

        return action
    }
}
