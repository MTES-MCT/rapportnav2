package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessActionCrossControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import org.slf4j.LoggerFactory

@UseCase
class UpdateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val processActionCrossControl: ProcessActionCrossControl
) {
    private val logger = LoggerFactory.getLogger(UpdateNavAction::class.java)

    fun execute(id: String, input: MissionNavAction): MissionNavActionEntity? {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        return try {
            val crossControl = processMissionActionCrossControl(
                input = input,
                actionType = action.actionType,
            )
            action.crossControl?.withId(crossControl?.id)
            missionActionRepository.save(action.toMissionActionModel())
            action.targets = processMissionActionTarget.execute(
                actionId = action.getActionId(),
                targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
            )
            action.computeCompleteness()
            action
        } catch (e: Exception) {
            logger.error("UpdateNavAction failed update Action", e)
            return null
        }
    }

    fun processMissionActionCrossControl(actionType: ActionType, input: MissionNavAction): CrossControlEntity?{
        if(actionType !== ActionType.CROSS_CONTROL) return null
        return  processActionCrossControl.execute(entity = MissionNavActionData.getCrossControlEntity(input))
    }
}
