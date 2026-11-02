package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import java.util.UUID

/**
 * Outcome of copying the PAM metrics of mission_general_info onto the ship rows of mission_resource_usage.
 * In a dry run, [created] / [updated] count what a real run would create / update.
 */
data class PamBackfillReport(
    val dryRun: Boolean,
    val scanned: Int,
    val created: Int,
    val updated: Int,
    val unchanged: Int,
    val skipped: List<PamBackfillSkip>
)

data class PamBackfillSkip(
    val generalInfoId: Int?,
    val missionId: Int?,
    val missionIdUUID: UUID?,
    val reason: PamBackfillSkipReason,
    val detail: String? = null
)

enum class PamBackfillSkipReason {
    /** The general info has no service, so its unit (and ship) is unknown. */
    NO_SERVICE,

    /** The service's control units have zero or several non-archived PATROL_BOAT in MonitorEnv. */
    NO_SHIP,

    /** Neither mission_id_uuid nor a local mission row for mission_id. */
    NO_MISSION_UUID,

    /** Another general info row of the same mission was already migrated (the lowest id wins). */
    DUPLICATE_MISSION,

    /** The write itself failed (e.g. the mission UUID doesn't exist in the mission table). */
    WRITE_FAILED
}
