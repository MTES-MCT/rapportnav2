package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.RecomputeMissionValidation
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class DeleteNavAction(
    private val deleteTarget: DeleteTarget,
    private val missionActionRepository: INavMissionActionRepository,
    private val recomputeMissionValidation: RecomputeMissionValidation
) {
    fun execute(id: UUID) {
        val action = missionActionRepository.findById(id).getOrNull()
        val ownerId = action?.ownerId
        deleteTarget.execute(actionId = action?.id, actionType = action?.actionType)
        missionActionRepository.deleteById(id)

        // Removing an action changes the mission's aggregate validity; re-persist it.
        recomputeMissionValidation.forNavMission(ownerId)
    }
}
