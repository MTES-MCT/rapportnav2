package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.ResolveActionOwnerId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData

@UseCase
class UpdateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val entityValidityValidator: EntityValidityValidator,
    private val getMissionDates: GetMissionDates,
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

        missionActionRepository.save(action.toMissionActionModel())
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )

        val missionDates = getMissionDates.execute(
            missionId = action.missionId,
            ownerId = action.ownerId,
            inquiryId = if (action.actionType == ActionType.INQUIRY) action.ownerId else null
        )
        val policy = ValidationPolicies.forMissionStartDate(missionDates?.startDateTimeUtc)
        action.computeValidity(validator = entityValidityValidator, policy = policy)

        return action
    }
}
