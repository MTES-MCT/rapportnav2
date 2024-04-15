package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionFreeNoteRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRescueRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import java.util.*

@UseCase
class GetNavActionByIdAndMissionId(
    private val statusActionsRepository: INavActionStatusRepository,
    private val controlActionsRepository: INavActionControlRepository,
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val freeNoteActionsRepository: INavActionFreeNoteRepository,
    private val rescueActionsRepository: INavActionRescueRepository,
    private val mapper: ObjectMapper
) {
    fun execute(id: UUID, missionId: Int, actionType: ActionType): NavActionEntity? {
        return when (actionType) {
            ActionType.CONTROL -> {
                controlActionsRepository.findById(id).orElse(null)?.run {
                    toActionControlEntity().let { actionControl ->
                        attachControlsToActionControl.toNavAction(
                            actionId = id.toString(),
                            action = actionControl
                        ).toNavAction()
                    }
                }
            }

            ActionType.STATUS -> {
                statusActionsRepository.findById(id).orElse(null)?.toActionStatusEntity()?.toNavActionEntity()
            }

            ActionType.NOTE -> {
                freeNoteActionsRepository.findById(id).orElse(null)?.toActionFreeNoteEntity()?.toNavAction()
            }

            ActionType.RESCUE -> {
                rescueActionsRepository.findById(id).orElse(null)?.toActionRescueEntity()?.toNavAction()
            }

            else -> null
        }
    }
}

