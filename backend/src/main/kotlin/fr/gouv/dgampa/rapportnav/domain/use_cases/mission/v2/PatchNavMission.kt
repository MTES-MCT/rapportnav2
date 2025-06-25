package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class PatchNavMission(
    private val repository: IMissionNavRepository,
    private val getNavMissionById2: GetNavMissionById2
) {
    private val logger = LoggerFactory.getLogger(PatchNavMission::class.java)

    fun execute(id: UUID, input: MissionNavInputEntity) : MissionNavEntity? {
        val missionFromDb = getNavMissionById2.execute(id) ?: return null
        if (missionFromDb.hasNotChanged(input)) return missionFromDb
        try {
            val model = missionFromDb.fromMissionNavInput(input)
            return MissionNavEntity.fromMissionModel(repository.save(model))
        }
        catch (e: Exception) {
            logger.error("Error while updating mission nav with id : $id", e)
            return null
        }
    }
}
