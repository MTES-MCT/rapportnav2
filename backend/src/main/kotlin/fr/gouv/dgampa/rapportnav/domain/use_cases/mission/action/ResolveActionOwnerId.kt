package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import java.util.UUID

/**
 * Resolves the owner (local mission UUID) an action belongs to from the identifier carried in the
 * `/owners/{ownerId}/actions` URL.
 *
 * The path segment is either the local mission UUID (v2 nav missions) or the legacy MonitorEnv/Fish
 * integer external id. This normalises both to the local mission UUID so writes always persist a
 * non-null `owner_id`. Returns null when the value is blank or no local mission row exists yet, in
 * which case callers keep whatever owner the request body carried.
 */
@UseCase
class ResolveActionOwnerId(
    private val getMissionByExternalId: GetMissionByExternalId,
) {
    fun execute(ownerId: String?): UUID? {
        if (ownerId.isNullOrBlank()) return null
        val trimmed = ownerId.trim()

        return try {
            UUID.fromString(trimmed)
        } catch (e: IllegalArgumentException) {
            getMissionByExternalId.execute(trimmed)?.id
        }
    }
}
