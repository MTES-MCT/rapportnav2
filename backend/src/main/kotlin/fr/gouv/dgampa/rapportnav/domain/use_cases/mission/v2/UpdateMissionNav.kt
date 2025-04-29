package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import org.slf4j.LoggerFactory
import java.time.Instant

@UseCase
class UpdateMissionNav(
    private val repository: IMissionNavRepository,
    private val getNavMissionById2: GetNavMissionById2
) {

    private val logger = LoggerFactory.getLogger(UpdateMissionNav::class.java)

    fun execute(missionId: String, startDateTimeUtc: Instant?, endDateTimeUtc: Instant?, isDelete: Boolean?, observationsByUnit: String?) : MissionNavEntity? {

        val missionFromDb = getNavMissionById2.execute(missionId) ?: return null

        val navEntity = missionFromDb.toMissionNavEntity(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDelete,
            observationsByUnit = observationsByUnit
        )

        if (missionFromDb.equalsInput(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = isDelete,
            observationsByUnit = observationsByUnit
        )) return navEntity

        try {
            val updatedMission = repository.save(navEntity.toMissionModel())
            return MissionNavEntity.fromMissionModel(updatedMission)
        }
        catch (e: Exception) {
            logger.error("Error while updating mission nav with id : ${missionId}", e)
            return null
        }
    }
}
