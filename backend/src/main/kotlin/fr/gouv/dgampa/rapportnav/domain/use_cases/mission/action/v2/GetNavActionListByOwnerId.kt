package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import java.util.*

@UseCase
class GetNavActionListByOwnerId(
    private val navMissionActionRepository: INavMissionActionRepository
) {
    fun execute(missionId: Int?): List<MissionNavActionEntity> {
        if (missionId == null) return listOf()
        return navMissionActionRepository.findByMissionId(missionId = missionId)
            .map { MissionNavActionEntity.fromMissionActionModel(it) }
    }

    fun execute(ownerId: UUID?): List<MissionNavActionEntity> {
        if (ownerId == null) return listOf()
        return navMissionActionRepository.findByOwnerId(ownerId = ownerId)
            .map { MissionNavActionEntity.fromMissionActionModel(it) }
    }
}
