package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetNavActionById(
    private val attachControlToAction: AttachControlToAction,
    private val missionActionRepository: INavMissionActionRepository,
) {
    private val logger = LoggerFactory.getLogger(GetNavActionById::class.java)

    fun execute(actionId: UUID?): MissionNavActionEntity? {
        if (actionId == null) {
            logger.error("GetNavActionById received a null actionId")
            throw IllegalArgumentException("GetNavActionById should not receive null missionId or actionId null")
        }
        val model = missionActionRepository.findById(actionId).orElse(null) ?: return null
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        val action = attachControlToAction.execute(entity) as MissionNavActionEntity;
        action.computeCompleteness()
        return action
    }
}
