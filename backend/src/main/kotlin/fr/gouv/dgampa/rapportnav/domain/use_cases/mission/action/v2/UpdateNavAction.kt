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
        return try {
            missionActionRepository.save(action)
            val controls = processMissionActionControl.execute(action)
            val infractions = processMissionActionInfraction.execute(action.getActionId(), controls)
            controls.processInfractions(infractions)
            action.computeControls(controls)
            action
        } catch (e: Exception) {
            logger.error("UpdateNavAction failed update Action", e)
            return null
        }
    }
}
