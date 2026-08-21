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
    companion object {
        // Wide default bounds so the list path can fetch nav missions without a date floor.
        private val FAR_PAST: Instant = Instant.EPOCH
        private val FAR_FUTURE: Instant = Instant.parse("9999-12-31T23:59:59Z")
    }

    /**
     * Nav-only missions for the paginated list: scoped to the user's service (env-mirror rows have a null
     * serviceId and are excluded by the query), newest first, with optional date bounds. Returns empty when the
     * user has no service (they own no nav-only missions of their own). Nav-only rows per service are few, so
     * they are fetched in full and windowed downstream together with the env missions.
     */
    fun executeForService(serviceId: Int?, startDateTimeUtc: Instant? = null, endDateTimeUtc: Instant? = null): List<MissionNavEntity> {
        if (serviceId == null) return emptyList()
        return repository.findNavMissionsForService(
            serviceId = serviceId,
            startedAfter = startDateTimeUtc ?: FAR_PAST,
            startedBefore = endDateTimeUtc ?: FAR_FUTURE
        )
            // safety: keep pure nav missions only (no MonitorEnv Int external id)
            .filter { it.externalId?.toIntOrNull() == null }
            .map { MissionNavEntity.fromMissionModel(it) }
    }

    fun execute(startDateTimeUtc: Instant, endDateTimeUtc: Instant? = null, serviceId: Int? = null, navMissionsOnly: Boolean? = false): List<MissionNavEntity>? {
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
                var result = missions
                if (serviceId != null) {
                    result = result.filter { it.serviceId == serviceId }
                }
                if (navMissionsOnly == true) {
                    // keep pure nav missions only: no external id, or one that isn't a MonitorEnv Int reference
                    result = result.filter { it.externalId?.toIntOrNull() == null }
                }
                result
            }
        return missionModelList.map { MissionNavEntity.fromMissionModel(it) }
    }
}
