package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

/**
 * One "load more" page of the mission list response.
 *
 * @property items the missions on this page (at most the requested `limit`; may be fewer when filters are active).
 * @property hasMore whether more missions exist beyond this page (derived from the raw stream, before filtering).
 * @property nextOffset the `offset` to request for the next page.
 */
data class MissionListPage(
    val items: List<MissionListItem>,
    val hasMore: Boolean,
    val nextOffset: Int,
)
