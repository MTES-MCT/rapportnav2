package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID

@UseCase
class GetEnvActionById(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction
) {
    fun execute(missionId: Int?, actionId: String): MissionEnvActionEntity? {
        if (missionId == null) return null
        if (!isValidUUID(actionId)) return null
        return try {
            val envAction = getEnvAction(missionId = missionId, actionId = actionId) ?: return null
            processEnvAction.execute(missionId = missionId, envAction = envAction)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetEnvActionById failed for missionId=$missionId, actionId=$actionId",
                originalException = e
            )
        }
    }

    private fun getEnvAction(missionId: Int, actionId: String): EnvActionEntity? {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions?.find { it.id.toString() == actionId }
    }
}
