package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetFishActionsByMissionId

@UseCase
class GetFishActionByIdAndMissionId(
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val mapper: ObjectMapper
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
