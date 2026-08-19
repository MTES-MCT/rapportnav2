package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType

/**
 * Placeholder for the upcoming mission-list filtering. Not applied yet — it exists so [GetMissionList] and
 * the controller can accept filter criteria without a signature churn when filtering lands. The list path
 * already loads each mission's actions (unvalidated), so filtering by [actionTypes] can be done in-memory
 * here once implemented.
 */
data class MissionListFilter(
    val actionTypes: List<ActionType>? = null,
)
