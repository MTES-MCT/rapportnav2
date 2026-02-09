package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import java.util.*

@UseCase
class GetNavActionListByOwnerId(
    private val navMissionActionRepository: INavMissionActionRepository
) {
    fun execute(missionId: Int?): List<NavActionEntity> {
        if (missionId == null) return listOf()
        return navMissionActionRepository.findByMissionId(missionId = missionId)
            .map { NavActionEntity.fromActionModel(it) }
    }

    fun execute(ownerId: UUID?): List<NavActionEntity> {
        if (ownerId == null) return listOf()
        return navMissionActionRepository.findByOwnerId(ownerId = ownerId)
            .map { NavActionEntity.fromActionModel(it) }
    }
}
