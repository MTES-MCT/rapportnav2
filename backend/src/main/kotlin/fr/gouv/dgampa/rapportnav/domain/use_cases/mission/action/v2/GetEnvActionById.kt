package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID

@UseCase
class GetEnvActionById(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction,
    private val missionNavRepository: IMissionNavRepository
) {
    fun execute(missionId: Int?, actionId: String): MissionEnvActionEntity? {
        if (!isValidUUID(actionId)) return null
        if (missionId == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "GetEnvActionById: missionId is required"
            )
        }
        val missionUUID = missionNavRepository.findByExternalId(missionId.toString()).orElse(null)?.id
            ?: return null
        val envAction = getEnvAction(missionId = missionId, actionId = actionId) ?: return null
        return processEnvAction.execute(ownerId = missionUUID, envAction = envAction)
    }

    private fun getEnvAction(missionId: Int, actionId: String): EnvActionEntity? {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions?.find { it.id.toString() == actionId }
    }
}
