package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionNavActionDataInput
import org.slf4j.LoggerFactory

@UseCase
class UpdateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val processMissionActionControl: ProcessMissionActionControl,
    private val processMissionActionInfraction: ProcessMissionActionInfraction
) {
    private val logger = LoggerFactory.getLogger(UpdateNavAction::class.java)

    fun execute(input: MissionActionInput): MissionNavActionEntity? {
        val action = MissionNavActionDataInput.toMissionNavActionEntity(input)
        val controlInputs = input.nav?.getControls(actionId = input.id, missionId = input.missionId)
        return try {
            missionActionRepository.save(action)
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
            logger.error("UpdateNavAction failed update Action", e)
            return null
        }
    }
}