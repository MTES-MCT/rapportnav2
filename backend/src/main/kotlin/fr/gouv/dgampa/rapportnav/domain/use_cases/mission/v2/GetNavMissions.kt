package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import java.time.Instant
import java.time.ZoneOffset

@UseCase
class GetNavMissions(
    private val repository: IMissionNavRepository
) {

    fun execute(startDateTimeUtc: Instant, endDateTimeUtc: Instant? = null, serviceId: Int? = null): List<MissionNavEntity>? {
        val missionModelList = repository.findAll(
            startBeforeDateTime = startDateTimeUtc,
            endBeforeDateTime = endDateTimeUtc ?: Instant.now()
                .atZone(ZoneOffset.UTC)
                .plusMonths(1)
                .withDayOfMonth(1)
                .toInstant(),
        )
            .filterNotNull()
            .let { missions ->
                if (serviceId != null) {
                    missions.filter { it.serviceId == serviceId }
                } else {
                    missions
                }
            }
        return missionModelList.map { MissionNavEntity.fromMissionModel(it) }
    }
}
