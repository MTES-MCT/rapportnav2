package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import java.time.Instant

@UseCase
class GetMissions(
    private val getEnvMissions: GetEnvMissions,
    private val getNavMissions: GetNavMissions,
    private val getComputeEnvMission: GetComputeEnvMission,
    private val getComputeNavMission: GetComputeNavMission,
    private val getControlUnitsForUser: GetControlUnitsForUser,
    private val getUserFromToken: GetUserFromToken,
) {
    /** One raw (un-computed) mission candidate in the merged stream, tagged by its source. */
    private data class RawRef(
        val startDateTimeUtc: Instant,
        val env: MissionEnvEntity? = null,
        val nav: MissionNavEntity? = null,
    )

    /**
     * Windowed, newest-first page of the merged env + nav stream. Env missions come from the MonitorEnv API
     * (control-unit scoped), nav-only missions from the local DB (service scoped). We merge the RAW candidates,
     * take the `[offset, offset+limit)` window, and compute (validate + sync) ONLY that window — so the expensive
     * per-mission compute runs on at most `limit` missions per request.
     *
     * `offset` indexes the raw merged stream; `nextOffset` is where the next page resumes.
     *
     * [predicate] filters computed missions *inside* the window loop: a candidate is only kept once it passes,
     * and the loop keeps consuming the raw stream until it has collected `limit` matches or the stream ends. This
     * makes `hasMore`/`nextOffset` reflect the *filtered* result, so an all-filtered-out window no longer reports
     * `hasMore = true` and sends "load more" walking the whole dataset one empty page at a time. Because the
     * predicate needs each mission computed, callers that apply a real filter must set [hasPostFilter] so the env
     * fetch is un-windowed (all candidates in range) — otherwise only one window's worth would be available to scan.
     */
    fun executePaginated(
        startDateTimeUtc: Instant? = null,
        endDateTimeUtc: Instant? = null,
        offset: Int = 0,
        limit: Int = 15,
        hasPostFilter: Boolean = false,
        predicate: (MissionEntity) -> Boolean = { true },
    ): PaginatedMissions {
        val user: User? = getUserFromToken.execute()

        // With a post-filter we may have to scan past the first window to collect `limit` matches, so fetch every
        // candidate in range; otherwise fetch just enough to fill the window and detect whether more exist (+1).
        val rawEnv: List<MissionEnvEntity> = getEnvMissions.execute(
            startedAfterDateTime = startDateTimeUtc,
            startedBeforeDateTime = endDateTimeUtc,
            pageNumber = if (hasPostFilter) null else 0,
            pageSize = if (hasPostFilter) null else offset + limit + 1,
            controlUnits = getControlUnitsForUser.execute()
        ).orEmpty()

        // Nav-only rows for the user's service are few → fetch them all and merge/window in memory.
        val rawNav: List<MissionNavEntity> = getNavMissions.executeForService(
            serviceId = user?.serviceId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc
        )

        val mergedRaw: List<RawRef> = (
            rawEnv.map { RawRef(startDateTimeUtc = it.startDateTimeUtc, env = it) } +
                rawNav.map { RawRef(startDateTimeUtc = it.startDateTimeUtc, nav = it) }
            ).sortedByDescending { it.startDateTimeUtc }

        // Walk the raw stream from `offset`, computing (validate + sync) each candidate, keeping only those that
        // pass `predicate`, until the page holds `limit` matches or the stream is exhausted. `i` ends on the first
        // unexamined raw index, so `hasMore`/`nextOffset` describe the *filtered* result.
        val collected = ArrayList<MissionEntity>(limit)
        var i = offset
        while (i < mergedRaw.size && collected.size < limit) {
            val ref = mergedRaw[i]
            val mission = ref.env?.let { getComputeEnvMission.execute(envMission = it) }
                ?: getComputeNavMission.execute(navMission = ref.nav!!)
            if (predicate(mission)) collected.add(mission)
            i++
        }

        return PaginatedMissions(
            missions = collected,
            hasMore = i < mergedRaw.size,
            nextOffset = i
        )
    }

    fun execute(startDateTimeUtc: Instant, endDateTimeUtc: Instant? = null): List<MissionEntity?> {
        val user: User? = getUserFromToken.execute()

        // needed to refetch from MonitorEnv and keep the local mission table in sync
        val envEntities: List<MissionEnvEntity>?  = getEnvMissions.execute(
            startedAfterDateTime = startDateTimeUtc,
            startedBeforeDateTime = endDateTimeUtc,
            pageNumber = null,
            pageSize = null,
            controlUnits = getControlUnitsForUser.execute()
        )

        // nav missions only, from the database — env-mirror rows are excluded to avoid duplicating
        // the missions already returned by MonitorEnv above.
        val navEntities = getNavMissions.execute(
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            serviceId = user?.serviceId,
            navMissionsOnly = true
        )

        val envMissions = envEntities?.map { getComputeEnvMission.execute(envMission = it) }.orEmpty()
        val navMissions = navEntities?.map { getComputeNavMission.execute(navMission = it) }.orEmpty()

        return(envMissions + navMissions).sortedByDescending { it.data?.startDateTimeUtc }

    }
}
