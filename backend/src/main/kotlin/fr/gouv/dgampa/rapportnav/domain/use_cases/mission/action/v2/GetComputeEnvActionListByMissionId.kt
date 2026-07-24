package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies

@UseCase
class GetComputeEnvActionListByMissionId(
    private val getEnvMissionById: GetEnvMissionById,
    private val processEnvAction: ProcessEnvAction
) {
    fun execute(missionId: Int, bypassValidation: Boolean = false): List<MissionEnvActionEntity> {
        val mission = getEnvMissionById.execute(missionId = missionId)
        val actions = mission?.envActions ?: listOf()

        // Every env action shares the mission's dates, so resolve the validation policy once instead of
        // per action. Skipped entirely when validation is bypassed (the policy is then unused).
        val policy = if (bypassValidation) null
        else ValidationPolicies.forMissionStartDate(mission?.startDateTimeUtc)

        return actions.filter { it.actionType !== ActionTypeEnum.NOTE }
            .map {
                processEnvAction.execute(
                    missionId = missionId,
                    envAction = it,
                    bypassValidation = bypassValidation,
                    policy = policy
                )
            }
    }
}
