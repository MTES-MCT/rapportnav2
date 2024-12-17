package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfractionEnvTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionEnvActionDataInput
import org.slf4j.LoggerFactory

@UseCase
class UpdateEnvAction(
    private val patchEnvAction: PatchEnvAction,
    private val processMissionActionControl: ProcessMissionActionControl,
    private val processMissionActionInfractionEnvTarget: ProcessMissionActionInfractionEnvTarget
) {

    private val logger = LoggerFactory.getLogger(UpdateEnvAction::class.java)
    fun execute(input: MissionActionInput): MissionEnvActionEntity? {
        val action = MissionEnvActionDataInput.toMissionEnvActionEntity(input)
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
            val controls = processMissionActionControl.execute(action)
            val infractions =
                processMissionActionInfractionEnvTarget.execute(action.getActionId(), input.env?.infractions)
            action.navInfractions = infractions
            action.computeControls(controls)
            action
        } catch (e: Exception) {
            logger.error("UpdateEnvAction failed update Action", e)
            return null
        }
    }
}
