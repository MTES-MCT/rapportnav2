package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.utils.toUuidOrNull

/**
 * Cloisonnement: a nav action may only be mutated through the owner (mission) it actually belongs to.
 *
 * `@CheckMissionAccess(param = "ownerId")` only proves the caller may touch the mission named in the
 * URL; without this check, pairing an owned `ownerId` with a foreign `actionId` would let the caller
 * overwrite / reparent or delete another mission's action (IDOR). The sibling env/fish use cases already
 * resolve the action within its mission, so this guards the nav case to the same standard.
 *
 * An action that does not exist yet is allowed through — the mutation then creates / no-ops it under the
 * already-authorized owner.
 */
@UseCase
class AssertNavActionBelongsToOwner(
    private val navActionRepository: INavMissionActionRepository,
) {
    /**
     * @param actionId the nav action UUID from the route.
     * @param ownerId the owner segment from the route (a mission UUID for nav).
     * @throws BackendUsageException with [BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION]
     * when the action exists but belongs to a different owner.
     */
    fun execute(actionId: String, ownerId: String) {
        // Only the action's owner is needed here, so read the row directly instead of running the full
        // GetNavActionById pipeline (targets / mission-dates / validity). That heavier load is left to the
        // handler, which actually returns the action — keeping this pre-handler containment check cheap.
        val id = actionId.toUuidOrNull() ?: return // not a nav action id (env/fish, or blank) → nothing to contain
        val existing = navActionRepository.findById(id).orElse(null) ?: return // not created yet → allowed
        if (existing.ownerId != ownerId.toUuidOrNull()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_ALLOWED_TO_PERFORM_EXCEPTION,
                message = "AssertNavActionBelongsToOwner: action does not belong to the owner in the URL"
            )
        }
    }
}
