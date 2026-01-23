package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import java.util.*

@UseCase
class PatchNavMission(
    private val repository: IMissionNavRepository,
    private val getNavMissionById2: GetNavMissionById2
) {
    fun execute(id: UUID, input: MissionNavInputEntity): MissionNavEntity? {
        val missionFromDb = getNavMissionById2.execute(id) ?: return null
        if (missionFromDb.hasNotChanged(input)) return missionFromDb
        val model = missionFromDb.fromMissionNavInput(input)
        return MissionNavEntity.fromMissionModel(repository.save(model))
    }
}
