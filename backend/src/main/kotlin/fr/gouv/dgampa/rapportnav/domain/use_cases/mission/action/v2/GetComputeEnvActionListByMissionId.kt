package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException

@UseCase
class GetComputeEnvActionListByMissionId(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction
) {
    fun execute(missionId: Int): List<MissionEnvActionEntity> {
        return try {
            val actions = getEnvActionList(missionId = missionId)
            actions.filter { it.actionType !== ActionTypeEnum.NOTE }
                .map {
                    processEnvAction.execute(missionId = missionId, envAction = it)
                }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetComputeEnvActionListByMissionId failed for missionId=$missionId",
                originalException = e
            )
        }
    }

    private fun getEnvActionList(missionId: Int): List<EnvActionEntity> {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions ?: listOf()
    }
}
