package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionData

@UseCase
class UpdateEnvAction(
    private val patchEnvAction: PatchEnvAction,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val getMissionDates: GetMissionDates
) {
    fun execute(id: String, input: MissionEnvAction): MissionEnvActionEntity {
        val action = MissionEnvActionData.toMissionEnvActionEntity(input)

        val missionDates = getMissionDates.execute(
            missionId = action.missionId,
            ownerId = null
        )
        if (missionDates != null && !action.isWithinMissionDates(missionDates.startDateTimeUtc, missionDates.endDateTimeUtc)) {
            throw BackendUsageException(code = BackendUsageErrorCode.DATES_OUTSIDE_MISSION_RANGE_EXCEPTION)
        }

        patchEnvAction.execute(
            ActionEnvInput(
                actionId = id,
                missionId = action.missionId,
                startDateTimeUtc = action.startDateTimeUtc,
                endDateTimeUtc = action.endDateTimeUtc,
                observationsByUnit = action.observationsByUnit
            )
        )
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )
        action.computeCompleteness()
        return action
    }
}
