package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import java.util.*

@UseCase
class GetNavActionByIdAndMissionId(
    private val statusActionsRepository: INavActionStatusRepository,
    private val controlActionsRepository: INavActionControlRepository,
    private val attachControlsToActionControl: AttachControlsToActionControl,
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
                        )?.toNavAction()
                    }
                }
            }

            ActionType.STATUS -> {
                statusActionsRepository.findById(id).orElse(null)?.toActionStatusEntity()?.toNavAction()
            }

            else -> null
        }
    }
}

