package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import org.slf4j.LoggerFactory
import java.util.UUID

@UseCase
class DeleteNavAction(
    private val missionActionRepository: INavMissionActionRepository,
) {
    private val logger = LoggerFactory.getLogger(DeleteNavAction::class.java)

    fun execute(id: UUID): Unit {
        return try {
            missionActionRepository.deleteById(id)
        } catch (e: Exception) {
            logger.error("DeleteNavAction failed delete Action", e)
        }
    }
}
