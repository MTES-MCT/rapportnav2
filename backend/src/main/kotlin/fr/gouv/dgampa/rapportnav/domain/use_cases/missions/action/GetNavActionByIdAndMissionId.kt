package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GetFishActionsByMissionId
import java.util.*

@UseCase
class GetNavActionByIdAndMissionId(
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
    private val statusActionsRepository: INavActionStatusRepository,
    private val controlActionsRepository: INavActionControlRepository,
    private val mapper: ObjectMapper
) {
    fun execute(id: UUID, missionId: Int, actionType: ActionType): NavActionEntity? =
        when (actionType) {
            ActionType.CONTROL -> controlActionsRepository.findById(id).orElse(null)
                ?.toActionControlEntity()?.toNavAction()

            ActionType.STATUS -> statusActionsRepository.findById(id).orElse(null)
                ?.toActionStatusEntity()?.toNavAction()

            else -> null
        }
}
