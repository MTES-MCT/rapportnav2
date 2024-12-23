package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControlEnvTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfractionEnvTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionEnvActionDataInput
import org.slf4j.LoggerFactory

@UseCase
class UpdateEnvAction(
    private val patchEnvAction: PatchEnvAction,
    private val processMissionActionControl: ProcessMissionActionControl,
    private val processMissionActionControlEnvTarget: ProcessMissionActionControlEnvTarget,
    private val processMissionActionInfractionEnvTarget: ProcessMissionActionInfractionEnvTarget
) {

    private val logger = LoggerFactory.getLogger(UpdateEnvAction::class.java)
    fun execute(input: MissionActionInput): MissionEnvActionEntity? {
        val action = MissionEnvActionDataInput.toMissionEnvActionEntity(input)
        val controlInputs = input.env?.getControls(actionId = input.id, missionId = input.missionId)
        return try {
            patchEnvAction.execute(
                ActionEnvInput(
                    actionId = input.id,
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

            val infractionInput = input.env?.getInfractions(
                missionId = input.missionId,
                actionId = action.getActionId()
            )

            infractionInput?.forEach {
               val controlId  = processMissionActionControlEnvTarget.execute(infraction = it,  controls = controls)
                it.controlId = controlId
            }

            val infractions = processMissionActionInfractionEnvTarget.execute(
                actionId = action.getActionId(),
                infractions = infractionInput
            )

            action.navInfractions = infractions
            action.computeControls(controls.toActionControlEntity())
            action
        } catch (e: Exception) {
            logger.error("UpdateEnvAction failed update Action", e)
            return null
        }
    }
}
