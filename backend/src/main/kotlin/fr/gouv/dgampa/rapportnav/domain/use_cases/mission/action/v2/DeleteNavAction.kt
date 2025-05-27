package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class DeleteNavAction(
    private val deleteTarget: DeleteTarget,
    private val deleteCrossControl: DeleteCrossControl,
    private val missionActionRepository: INavMissionActionRepository
) {
    private val logger = LoggerFactory.getLogger(DeleteNavAction::class.java)

    fun execute(id: UUID): Unit {
        return try {
            val action = missionActionRepository.findById(id).getOrNull()
            deleteTarget.execute(actionId = action?.id, actionType = action?.actionType)
            deleteCrossControl.execute(crossControlId = action?.crossControlId, actionType = action?.actionType)
            missionActionRepository.deleteById(id)
        } catch (e: Exception) {
            logger.error("DeleteNavAction failed delete Action", e)
        }
    }
}
