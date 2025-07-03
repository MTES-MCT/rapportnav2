package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import org.slf4j.LoggerFactory

@UseCase
class UpdateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val processMissionActionTarget: ProcessMissionActionTarget
) {
    private val logger = LoggerFactory.getLogger(UpdateNavAction::class.java)

    fun execute(id: String, input: MissionNavAction): MissionNavActionEntity? {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        return try {
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
}
