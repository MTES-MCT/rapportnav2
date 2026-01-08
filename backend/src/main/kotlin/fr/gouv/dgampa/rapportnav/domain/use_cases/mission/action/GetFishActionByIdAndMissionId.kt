package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetFishActionsByMissionId
import tools.jackson.databind.json.JsonMapper

@UseCase
class GetFishActionByIdAndMissionId(
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val mapper: JsonMapper
) {
    fun execute(id: Int, missionId: Int): ExtendedFishActionEntity? {
        return getFishActionsByMissionId
            .execute(missionId = missionId)
            .firstOrNull { it.controlAction?.action?.id == id }
            ?.let {
                attachControlsToActionControl.toFishAction(
                    actionId = id.toString(),
                    action = it
                )
            }
    }
}
