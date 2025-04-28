package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import java.time.Instant
import java.time.ZoneOffset

@UseCase
class GetNavMissions(
    private val repository: IMissionNavRepository
) {

    fun execute(startDateTimeUtc: Instant, endDateTimeUtc: Instant? = null): List<MissionEntity>? {
        val missionModelList = repository.findAll(
            startBeforeDateTime = startDateTimeUtc,
            endBeforeDateTime = endDateTimeUtc ?: Instant.now()
                .atZone(ZoneOffset.UTC)
                .plusMonths(1)
                .withDayOfMonth(1)
                .toInstant()
        )

        return missionModelList.filterNotNull().map { MissionEntity.fromMissionNavModel(it) }
    }
}
