package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetComputeNavActionListByMissionId(
    private val processNavAction: ProcessNavAction,
    private val navMissionActionRepository: INavMissionActionRepository,
) {
    private val logger = LoggerFactory.getLogger(GetComputeNavActionListByMissionId::class.java)

    fun execute(missionId: Int?): List<MissionNavActionEntity> {
        if (missionId == null) {
            logger.error("GetComputeNavActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetComputeNavActionListByMissionId should not receive null missionId")
        }

        return try {
            val actions = getNavActionList(missionId = missionId)
            actions.map { processNavAction.execute(action = it) }
        } catch (e: Exception) {
            logger.error("GetComputeNavActionListByMissionId failed loading Actions", e)
            return listOf()
        }
    }

    fun execute(ownerId: UUID?): List<MissionNavActionEntity> {
        if (ownerId == null) {
            logger.error("GetComputeNavActionListByMissionIdString received a null missionId")
            throw IllegalArgumentException("GetComputeNavActionListByMissionIdString should not receive null missionId")
        }

        return try {
            val actions = getNavActionList(ownerId = ownerId)
            actions.map { processNavAction.execute(action = it) }
        } catch (e: Exception) {
            logger.error("GetComputeNavActionListByMissionIdString failed loading Actions", e)
            return listOf()
        }
    }

    private fun getNavActionList(missionId: Int): List<MissionActionModel> {
        return navMissionActionRepository.findByMissionId(missionId = missionId).orEmpty()
    }

    private fun getNavActionList(ownerId: UUID): List<MissionActionModel> {
        return navMissionActionRepository.findByOwnerId(ownerId = ownerId).orEmpty()
    }
}
