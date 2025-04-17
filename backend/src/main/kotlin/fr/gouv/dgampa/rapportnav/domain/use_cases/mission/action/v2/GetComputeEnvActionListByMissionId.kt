package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import org.slf4j.LoggerFactory

@UseCase
class GetComputeEnvActionListByMissionId(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction
) {
    private val logger = LoggerFactory.getLogger(GetComputeFishActionListByMissionId::class.java)

    fun execute(missionId: String?): List<MissionEnvActionEntity> {
        if (missionId == null) {
            logger.error("GetComputeEnvActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetComputeEnvActionListByMissionId should not receive null missionId")
        }
        return try {
            val actions = getEnvActionList(missionId = missionId)
            actions.map {
                processEnvAction.execute(missionId = missionId, envAction = it)
            }
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
        }
    }

    private fun getEnvActionList(missionId: String): List<EnvActionEntity> {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions ?: listOf()
    }
}
