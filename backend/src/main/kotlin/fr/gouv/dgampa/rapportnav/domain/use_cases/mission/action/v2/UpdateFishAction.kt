package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionFishInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishActionData
import org.slf4j.LoggerFactory

@UseCase
class UpdateFishAction(
    private val patchFishAction: PatchFishAction,
    private val processMissionActionTarget: ProcessMissionActionTarget
) {
    private val logger = LoggerFactory.getLogger(UpdateFishAction::class.java)

    fun execute(id: String, input: MissionFishAction): MissionFishActionEntity? {
        val action = MissionFishActionData.toMissionFishActionEntity(input)
        return try {
            patchFishAction.execute(
                ActionFishInput(
                    actionId = id,
                    missionId = action.missionId,
                    startDateTimeUtc = action.startDateTimeUtc,
                    endDateTimeUtc = action.endDateTimeUtc,
                    observationsByUnit = action.observationsByUnit
                )
            )
            val targets = processMissionActionTarget.execute(
                actionId = action.getActionId(),
                targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
            )
            action.targets = targets
            action.computeCompleteness()
            action
        } catch (e: Exception) {
            logger.error("UpdateFishAction failed update Action", e)
            return null
        }
    }
}
