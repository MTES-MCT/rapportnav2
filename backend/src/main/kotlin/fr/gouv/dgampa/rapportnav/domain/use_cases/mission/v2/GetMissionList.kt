package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionListItem
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionListPage
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
     * Returns one windowed page of the mission list. Dates are optional (no date filter → newest missions overall).
     * The three filters (status / completeness / report type) are applied *inside* [GetMissions.executePaginated]'s
     * window loop — it keeps consuming the raw stream until the page holds `limit` matches or the stream ends — so
     * [MissionListPage.hasMore] reflects the filtered result and "load more" stops once matches are exhausted rather
     * than walking the whole dataset one all-filtered-out page at a time.
     *
     * @param filter optional multi-select filtering. A null filter (or null/empty list per dimension) matches all.
     */
    fun execute(
        startDateTimeUtc: Instant? = null,
        endDateTimeUtc: Instant? = null,
        filter: MissionListFilter? = null,
        offset: Int = 0,
        limit: Int = 15,
    ): MissionListPage {
        val page = getMissions.executePaginated(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            offset = offset,
            limit = limit,
            hasPostFilter = hasActiveFilter(filter),
            predicate = { entity -> matches(MissionListItem.fromMissionEntity(entity), filter) }
        )
        val items = page.missions.map { MissionListItem.fromMissionEntity(it) }
        return MissionListPage(items = items, hasMore = page.hasMore, nextOffset = page.nextOffset)
    }

    /** True when at least one dimension is actually constrained → the raw stream must be fully scanned to page. */
    private fun hasActiveFilter(filter: MissionListFilter?): Boolean {
        if (filter == null) return false
        return !filter.statuses.isNullOrEmpty() ||
            !filter.completenessStatuses.isNullOrEmpty() ||
            !filter.reportTypes.isNullOrEmpty()
    }

    /** A null/empty list on a dimension means that dimension is not filtered; dimensions combine with AND. */
    private fun matches(item: MissionListItem, filter: MissionListFilter?): Boolean {
        if (filter == null) return true
        val statusOk = filter.statuses.isNullOrEmpty() || filter.statuses.contains(item.status)
        val completenessOk = filter.completenessStatuses.isNullOrEmpty() ||
            filter.completenessStatuses.contains(item.completenessForStats?.status)
        val reportTypeOk = filter.reportTypes.isNullOrEmpty() ||
            filter.reportTypes.contains(item.missionReportType)
        return statusOk && completenessOk && reportTypeOk
    }
}
