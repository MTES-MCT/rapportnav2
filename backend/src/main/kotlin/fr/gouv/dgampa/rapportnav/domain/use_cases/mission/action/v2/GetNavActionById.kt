package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetNavActionById(
    private val processNavAction: ProcessNavAction,
    private val missionActionRepository: INavMissionActionRepository,
) {
    private val logger = LoggerFactory.getLogger(GetNavActionById::class.java)

    fun execute(actionId: String?): MissionNavActionEntity? {
        if (!isValidUUID(actionId)) return null
        return try {
            val model = missionActionRepository.findById(UUID.fromString(actionId)).orElse(null) ?: return null
            processNavAction.execute(action = model)
        } catch (e: Exception) {
            logger.error("GetNavActionById failed loading action", e)
            return null
        }
    }
}
