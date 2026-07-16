package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import java.util.*

@UseCase
class GetNavMissionById(
    private val repository: IMissionNavRepository
) {
    fun execute(id: UUID): MissionNavEntity? {
        val mission = repository.findById(id).orElse(null) ?: return null
        return MissionNavEntity.fromMissionModel(mission)
    }
}
