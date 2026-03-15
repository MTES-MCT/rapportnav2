package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData

@UseCase
class UpdateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val getMissionDates: GetMissionDates
) {
    fun execute(id: String, input: MissionNavAction): MissionNavActionEntity {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        if (id != input.id) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "UpdateNavAction: action id mismatch"
            )
        }

        val missionDates = getMissionDates.execute(
            missionId = action.missionId,
            ownerId = action.ownerId,
            inquiryId = if (action.actionType == ActionType.INQUIRY) action.ownerId else null
        )
        if (missionDates != null && !action.isWithinMissionDates(missionDates.startDateTimeUtc, missionDates.endDateTimeUtc)) {
            throw BackendUsageException(code = BackendUsageErrorCode.DATES_OUTSIDE_MISSION_RANGE_EXCEPTION)
        }

        missionActionRepository.save(action.toMissionActionModel())
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )
        action.computeCompleteness()
        return action
    }
}
