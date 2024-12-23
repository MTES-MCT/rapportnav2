package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionFishInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionFishActionDataInput
import org.slf4j.LoggerFactory

@UseCase
class UpdateFishAction(
    private val patchFishAction: PatchFishAction,
    private val processMissionActionControl: ProcessMissionActionControl,
    private val processMissionActionInfraction: ProcessMissionActionInfraction
) {
    private val logger = LoggerFactory.getLogger(UpdateFishAction::class.java)

    fun execute(input: MissionActionInput): MissionFishActionEntity? {
        val action = MissionFishActionDataInput.toMissionFishActionEntity(input)
        val controlInputs = input.fish?.getControls(actionId = input.id, missionId = input.missionId)
        return try {
            patchFishAction.execute(
                ActionFishInput(
                    actionId = action.id.toString(),
                    missionId = action.missionId,
                    startDateTimeUtc = action.startDateTimeUtc,
                    endDateTimeUtc = action.endDateTimeUtc,
                    observationsByUnit = action.observationsByUnit
                )
            )
            val controls = processMissionActionControl.execute(
                controls = controlInputs,
                actionId = action.getActionId()
            )
            val infractions = processMissionActionInfraction.execute(
                actionId = action.getActionId(),
                infractions = controls.getAllInfractions()
            )
            action.computeControls(controls.toActionControlEntity(infractions))
            action.computeCompleteness()
            action
        } catch (e: Exception) {
            logger.error("UpdateFishAction failed update Action", e)
            return null
        }
    }
}
