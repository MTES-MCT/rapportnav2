package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.slf4j.LoggerFactory

@UseCase
class GetNavActionListByMissionId(
    private val navMissionActionRepository: INavMissionActionRepository,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2
): AbstractGetMissionAction(getStatusForAction, getControlByActionId) {
    private val logger = LoggerFactory.getLogger(GetNavActionListByMissionId::class.java)

    fun execute(missionId: Int?): List<MissionNavActionEntity> {
        if (missionId == null) {
            logger.error("GetNavActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetNavActionListByMissionId should not receive null missionId")
        }
        return try {
            val actions = getNavActionList(missionId = missionId)
            processActions( actions = actions)
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
        }
    }

    private fun getNavActionList(missionId: Int): List<MissionActionModel> {
        return navMissionActionRepository.findByMissionId(missionId = missionId).orEmpty()
    }

    private fun processActions(actions: List<MissionActionModel>): List<MissionNavActionEntity> {
        return actions.map {
            val action = MissionNavActionEntity.fromMissionActionModel(it)
            action.status = this.getStatus(action)
            action.computeControls(this.getControls(action))
            action.computeCompleteness()
            action
        }
    }

}
