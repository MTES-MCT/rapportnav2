package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies

@UseCase
class GetComputeFishActionListByMissionId(
    private val processFishAction: ProcessFishAction,
    private val getFishActionListByMissionId: GetFishActionListByMissionId,
    private val getMissionDates: GetMissionDates
) {
    fun execute(missionId: Int, bypassValidation: Boolean = false): List<MissionFishActionEntity> {
        val actions = getFishActionList(missionId = missionId)

        // Every fish action shares the mission's dates, so resolve the validation policy once instead of
        // per action. Skipped entirely when validation is bypassed (the policy is then unused).
        val policy = if (bypassValidation) null
        else ValidationPolicies.forMissionStartDate(
            getMissionDates.execute(missionId = missionId, ownerId = null)?.startDateTimeUtc
        )

        return actions.map {
            processFishAction.execute(
                missionId = missionId,
                action = it,
                bypassValidation = bypassValidation,
                policy = policy
            )
        }
    }

    private fun getFishActionList(missionId: Int): List<MissionAction> {
        return getFishActionListByMissionId.execute(missionId = missionId).filter {
            listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL
            ).contains(it.actionType)
        }
    }
}
