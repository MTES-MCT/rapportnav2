package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable


@UseCase
class GetFishActionsByMissionId(
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val fishActionRepo: IFishActionRepository,
    private val getFakeActionData: FakeActionData
) {

    private val logger = LoggerFactory.getLogger(GetFishActionsByMissionId::class.java)

    /**
     * This function has two aims:
     * - only keep the allowed control types
     * - attach Nav data to a Fish Action
     */
    private fun filterAndAttachControls(fishActions: List<MissionAction>): List<ExtendedFishActionEntity> {
        return fishActions.filter {
            listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL
            ).contains(it.actionType)
        }.map {
            var action = ExtendedFishActionEntity.fromMissionAction(it)
            action = attachControlsToActionControl.toFishAction(
                actionId = it.id?.toString(),
                action = action
            )
            // recompute completeness once controls have been attached
            action.controlAction?.computeCompleteness()
            action
        }
    }

    @Cacheable(value = ["fishActions"], key = "#missionId")
    fun execute(missionId: Int?): List<ExtendedFishActionEntity> {
        if (missionId == null) {
            logger.error("GetFishActionsByMissionId received a null missionId")
            throw IllegalArgumentException("GetFishActionsByMissionId should not receive null missionId")
        }
        return try {
            val fishActions = fishActionRepo.findFishActions(missionId = missionId)
            // Filtering actions based on action type
            val actions = filterAndAttachControls(fishActions)
            actions
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
//            return listOf()
            // TODO
            val actions = getFakeActionData.getFakeFishActions(missionId)
            return filterAndAttachControls(actions)
        }
    }
}
