package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class DeleteNavAction(
    private val deleteTarget: DeleteTarget,
    private val missionActionRepository: INavMissionActionRepository
) {
    fun execute(id: UUID) {
        val action = missionActionRepository.findById(id).getOrNull()
        deleteTarget.execute(actionId = action?.id, actionType = action?.actionType)
        missionActionRepository.deleteById(id)
    }
}
