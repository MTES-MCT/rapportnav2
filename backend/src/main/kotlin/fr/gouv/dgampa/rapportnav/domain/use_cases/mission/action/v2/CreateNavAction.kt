package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import org.slf4j.LoggerFactory

@UseCase
class CreateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val getComputeTarget: GetComputeTarget,
) {
    private val logger = LoggerFactory.getLogger(CreateNavAction::class.java)

    fun execute(input: MissionAction): MissionNavActionEntity? {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        return try {
            var savedAction = MissionNavActionEntity.fromMissionActionModel(missionActionRepository.save(action.toMissionActionModel()))

            // when controls, precompute targets
            if (savedAction.actionType === ActionType.CONTROL) {
                savedAction.targets = getComputeTarget.execute(actionId = savedAction.id.toString(), isControl = true)
            }

            savedAction
        } catch (e: Exception) {
            logger.error("CreateNavAction failed update Action", e)
            return null
        }
    }
}
