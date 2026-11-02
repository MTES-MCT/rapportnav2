package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum

/**
 * Per-source recap of a mission's actions, shown in the ULAM mission list expanded row.
 * Counts are 1-per-action (env's `actionNumberOfControls` is intentionally ignored).
 *
 * Which counts the frontend renders depends on the source:
 *  - MONITORFISH  -> controls only,
 *  - MONITORENV   -> controls + surveillances,
 *  - RAPPORT_NAV  -> controls + surveillances + "autres actions".
 */
data class MissionActionSummaryEntity(
    val source: MissionSourceEnum,
    val nbControls: Int = 0,
    val nbSurveillances: Int = 0,
    val nbOtherActions: Int = 0,
)
