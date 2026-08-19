package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionListItem
import java.time.Instant

/**
 * Light counterpart of [GetMissions] for the mission **list** page: it runs the exact same fetch + compute
 * orchestration as [GetMissions] (full validation + `SyncMissionValidation` write-back, unchanged) and only
 * projects each resulting [MissionEntity][fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity]
 * to the light [MissionListItem] the list renders. The sole difference from the previous list endpoint is a
 * smaller response payload — no behavior change.
 */
@UseCase
class GetMissionList(
    private val getMissions: GetMissions,
) {
    /**
     * @param filter reserved for the upcoming list filtering — NOT applied yet.
     * @param pageNumber / @param pageSize reserved for the upcoming "load more" pagination — NOT applied yet.
     */
    fun execute(
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
        filter: MissionListFilter? = null,
        pageNumber: Int? = null,
        pageSize: Int? = null,
    ): List<MissionListItem> {
        return getMissions.execute(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc
        )
            .filterNotNull()
            .map { MissionListItem.fromMissionEntity(it) }
    }
}
