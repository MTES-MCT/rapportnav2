package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.*
import java.util.*

@UseCase
class GetNavActionByIdAndMissionId(
    private val statusActionsRepository: INavActionStatusRepository,
    private val controlActionsRepository: INavActionControlRepository,
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val freeNoteActionsRepository: INavActionFreeNoteRepository,
    private val rescueActionsRepository: INavActionRescueRepository,
    private val nauticalEventRepository: INavActionNauticalEventRepository,
    private val vigimerRepository: INavActionVigimerRepository,
    private val antiPollutionRepository: INavActionAntiPollutionRepository,
    private val baaemRepository: INavActionBAAEMRepository,
    private val publicOrderRepository: INavActionPublicOrderRepository,
    private val representationRepository: INavActionRepresentationRepository,
    private val illegalImmigrationRepository: INavActionIllegalImmigrationRepository,
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
                        ).toNavActionEntity()
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

            ActionType.NAUTICAL_EVENT -> {
                nauticalEventRepository.findById(id).orElse(null)?.toActionNauticalEventEntity()?.toNavAction()
            }

            ActionType.VIGIMER -> {
                vigimerRepository.findById(id).orElse(null)?.toActionVigimerEntity()?.toNavAction()
            }

            ActionType.BAAEM_PERMANENCE -> {
                baaemRepository.findById(id).orElse(null)?.toActionBAAEMPermanenceEntity()?.toNavAction()
            }

            ActionType.ANTI_POLLUTION -> {
                antiPollutionRepository.findById(id).orElse(null)?.toAntiPollutionEntity()?.toNavAction()
            }
            ActionType.PUBLIC_ORDER -> {
                publicOrderRepository.findById(id).orElse(null)?.toPublicOrderEntity()?.toNavAction()
            }
            ActionType.REPRESENTATION -> {
                representationRepository.findById(id).orElse(null)?.toRepresentationEntity()?.toNavAction()
            }
            ActionType.ILLEGAL_IMMIGRATION -> {
                illegalImmigrationRepository.findById(id).orElse(null)?.toActionIllegalImmigrationEntity()?.toNavAction()
            }

            else -> null
        }
    }
}

