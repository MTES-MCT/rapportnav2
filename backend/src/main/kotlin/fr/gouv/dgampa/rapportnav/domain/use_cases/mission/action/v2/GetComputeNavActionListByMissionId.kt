package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetComputeNavActionListByMissionId(
    private val processNavAction: ProcessNavAction,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId
) {
    private val logger = LoggerFactory.getLogger(GetComputeNavActionListByMissionId::class.java)

    fun execute(missionId: Int?): List<MissionNavActionEntity> {
        if (missionId == null) return listOf()
        return try {
            val actions = getNavActionListByOwnerId.execute(missionId = missionId)
            actions.map { processNavAction.execute(action = it) }
        } catch (e: Exception) {
            logger.error("GetComputeNavActionListByMissionId failed loading Actions", e)
            return listOf()
        }
    }

    fun execute(ownerId: UUID?): List<MissionNavActionEntity> {
        if (ownerId == null) return listOf()
        return try {
            val actions = getNavActionListByOwnerId.execute(ownerId = ownerId)
            actions.map { processNavAction.execute(action = it) }
        } catch (e: Exception) {
            logger.error("GetComputeNavActionListByMissionIdString failed loading Actions", e)
            return listOf()
        }
    }
}
