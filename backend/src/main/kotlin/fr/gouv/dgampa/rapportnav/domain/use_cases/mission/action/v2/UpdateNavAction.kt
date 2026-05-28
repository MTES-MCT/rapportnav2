package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRepository

@UseCase
class UpdateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val agentRepository: IDBAgentRepository
) {
    fun execute(id: String, input: MissionNavAction): MissionNavActionEntity {
        val action = MissionNavActionData.toMissionNavActionEntity(input)
        if (id != input.id) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "UpdateNavAction: action id mismatch"
            )
        }

        val model = action.toMissionActionModel()

        // save agents participating to mission
        val agentIds = input.data.agentIds
        if (!agentIds.isNullOrEmpty()) {
            model.agents = agentRepository.findAllById(agentIds).toMutableList()
        }
        missionActionRepository.save(model)
        action.agentIds = agentIds ?: emptyList()

        // save targets data
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )

        action.computeCompleteness()
        return action
    }
}
