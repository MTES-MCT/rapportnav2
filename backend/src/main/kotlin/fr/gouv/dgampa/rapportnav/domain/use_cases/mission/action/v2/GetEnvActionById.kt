package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID

@UseCase
class GetEnvActionById(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction
) {
    fun execute(missionId: Int?, actionId: String): fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity? {
        if (!isValidUUID(actionId)) return null
        if (missionId == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "GetEnvActionById: missionId is required"
            )
        }
        val envAction = getEnvAction(missionId = missionId, actionId = actionId) ?: return null
        return processEnvAction.execute(missionId = missionId, envAction = envAction)
    }

    private fun getEnvAction(missionId: Int, actionId: String): EnvActionEntity? {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions?.find { it.id.toString() == actionId }
    }
}
