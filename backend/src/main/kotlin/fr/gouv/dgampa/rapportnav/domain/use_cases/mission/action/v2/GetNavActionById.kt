package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetNavActionById(
    private val missionActionRepository: INavMissionActionRepository,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
): GetMissionAction(getStatusForAction, getControlByActionId)  {
    private val logger = LoggerFactory.getLogger(GetNavActionById::class.java)

    fun execute(actionId: UUID?): MissionNavActionEntity? {
        if (actionId == null) {
            logger.error("GetNavActionById received a null actionId")
            throw IllegalArgumentException("GetNavActionById should not receive null missionId or actionId null")
        }
        val model = missionActionRepository.findById(actionId).orElse(null) ?: return null
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        entity.computeControls(controls = this.getControls(entity))
        entity.computeCompleteness()
        return entity
    }
}
