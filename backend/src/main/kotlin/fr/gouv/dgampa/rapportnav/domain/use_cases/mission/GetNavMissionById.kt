package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import org.slf4j.LoggerFactory

@UseCase
class GetNavMissionById(
    private val navActionControlRepository: INavActionControlRepository,
    private val navStatusRepository: INavActionStatusRepository,
    private val navFreeNoteRepository: INavActionFreeNoteRepository,
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val navRescueRepository: INavActionRescueRepository,
    private val navNauticalEventRepository: INavActionNauticalEventRepository,
    private val navVigimerRepository: INavActionVigimerRepository,
    private val navAntiPollutionRepository: INavActionAntiPollutionRepository,
    private val navBAAEMRepository: INavActionBAAEMRepository,
    private val navPublicOrderRepository: INavActionPublicOrderRepository,
    private val navRepresentationRepository: INavActionRepresentationRepository
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

        val rescues = navRescueRepository.findAllByMissionId(missionId = missionId)
            .map { it.toActionRescueEntity() }
            .map { it.toNavAction() }

        val nauticalEvents = navNauticalEventRepository.findAllByMissionId(missionId = missionId)
            .map { it.toActionNauticalEventEntity() }
            .map { it.toNavAction() }

        val vigimers = navVigimerRepository.findAllByMissionId(missionId = missionId)
            .map { it.toActionVigimerEntity() }
            .map { it.toNavAction() }

        val antiPollutions = navAntiPollutionRepository.findAllByMissionId(missionId = missionId)
            .map { it.toAntiPollutionEntity() }
            .map { it.toNavAction() }

        val baaemPermanences = navBAAEMRepository.findAllByMissionId(missionId = missionId)
            .map { it.toActionBAAEMPermanenceEntity() }
            .map { it.toNavAction() }

        val publicOrders = navPublicOrderRepository.findAllByMissionId(missionId = missionId)
            .map { it.toPublicOrderEntity() }
            .map { it.toNavAction() }

        val representations = navRepresentationRepository.findAllByMissionId(missionId = missionId)
            .map { it.toRepresentationEntity() }
            .map { it.toNavAction() }

        val actions = controls + statuses + notes + rescues + nauticalEvents + vigimers + antiPollutions + baaemPermanences + representations + publicOrders
        val mission = NavMissionEntity(id = missionId, actions = actions)
        return mission
    }

}
