package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.ResolveActionOwnerId
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData

@UseCase
class CreateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val entityValidityValidator: EntityValidityValidator,
    private val computeActionValidityAndRecomputeMission: ComputeActionValidityAndRecomputeMission,
    private val resolveActionOwnerId: ResolveActionOwnerId
) {
    fun execute(input: MissionAction, ownerId: String? = null): MissionNavActionEntity {
        val action = MissionNavActionData.toMissionNavActionEntity(input)

        // Stamp the owner from the authoritative URL owner so the row is never persisted without an
        // ownerId, regardless of what the client put (or omitted) in the body. Falls back to the body
        // value when the path owner can't be resolved.
        resolveActionOwnerId.execute(ownerId)?.let { action.ownerId = it }

        entityValidityValidator.validateAndThrow(action, ValidateThrowsBeforeSave::class.java)

        // Compute the action's completeness BEFORE saving, since the nav row carries isCompleteForStats.
        val inquiryId = if (action.actionType == ActionType.INQUIRY) action.ownerId else null
        computeActionValidityAndRecomputeMission.computeActionValidity(
            action = action,
            ownerId = action.ownerId,
            inquiryId = inquiryId
        )

        val saved = MissionNavActionEntity.fromMissionActionModel(missionActionRepository.save(action.toMissionActionModel()))

        // Recompute the mission AFTER the save, so the aggregate re-reads the now-persisted action.
        computeActionValidityAndRecomputeMission.recomputeMission(action = action, ownerId = action.ownerId)

        return saved
    }
}
