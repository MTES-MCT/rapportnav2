package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity

/**
 * A windowed page of fully-computed missions produced by [GetMissions.executePaginated].
 *
 * @property missions the computed missions for this window (at most `limit`), newest first.
 * @property hasMore whether more raw missions exist beyond this window (computed before any downstream filtering).
 * @property nextOffset the raw-stream offset to request for the next page.
 */
data class PaginatedMissions(
    val missions: List<MissionEntity>,
    val hasMore: Boolean,
    val nextOffset: Int,
)
