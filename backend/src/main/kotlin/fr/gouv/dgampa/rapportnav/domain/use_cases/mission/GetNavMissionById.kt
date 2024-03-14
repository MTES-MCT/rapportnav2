package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionFreeNoteRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import org.slf4j.LoggerFactory

@UseCase
class GetNavMissionById(
    private val navActionControlRepository: INavActionControlRepository,
    private val navStatusRepository: INavActionStatusRepository,
    private val navFreeNoteRepository: INavActionFreeNoteRepository,
    private val attachControlsToActionControl: AttachControlsToActionControl,
) {
    private val logger = LoggerFactory.getLogger(GetNavMissionById::class.java)

    fun execute(missionId: Int): NavMissionEntity {
        val controls = navActionControlRepository
            .findAllByMissionId(missionId = missionId)
            .map { actionControl ->
                attachControlsToActionControl.toNavAction(
                    actionId = actionControl.id.toString(),
                    action = actionControl
                ).toNavAction()
            }


        val statuses = navStatusRepository.findAllByMissionId(missionId = missionId)
            .map { it.toActionStatusEntity() }
            .map { it.toNavActionEntity() }

        val notes = navFreeNoteRepository.findAllByMissionId(missionId = missionId)
            .map { it.toActionFreeNoteEntity() }
            .map { it.toNavAction() }

        val actions = controls + statuses + notes
        val mission = NavMissionEntity(id = missionId, actions = actions)
        return mission
    }

}
