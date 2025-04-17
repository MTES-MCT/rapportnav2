package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.slf4j.LoggerFactory

@UseCase
class GetComputeNavActionListByMissionId(
    private val processNavAction: ProcessNavAction,
    private val navMissionActionRepository: INavMissionActionRepository,
) {
    private val logger = LoggerFactory.getLogger(GetComputeNavActionListByMissionId::class.java)

    fun execute(missionId: String?): List<MissionNavActionEntity> {
        if (missionId == null) {
            logger.error("GetComputeNavActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetComputeNavActionListByMissionId should not receive null missionId")
        }
        return try {
            val actions = getNavActionList(missionId = missionId)
            actions.map { processNavAction.execute(missionId = missionId, action = it) }
        } catch (e: Exception) {
            logger.error("GetComputeNavActionListByMissionId failed loading Actions", e)
            return listOf()
        }
    }

    private fun getNavActionList(missionId: String): List<MissionActionModel> {
        return navMissionActionRepository.findByMissionId(missionId = missionId).orEmpty()
    }
}
