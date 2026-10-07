package fr.gouv.dgampa.rapportnav.infrastructure.api.security

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.AssertNavActionBelongsToOwner
import org.springframework.stereotype.Component

/**
 * [ChildAccessGuard] for nav actions: a nav action may only be reached through the owner (mission) it
 * belongs to. Delegates to the pure domain use case [AssertNavActionBelongsToOwner].
 */
@Component
class NavActionAccessGuard(
    private val assertNavActionBelongsToOwner: AssertNavActionBelongsToOwner,
) : ChildAccessGuard {
    override val resource = MissionChildResource.NAV_ACTION

    override fun assertBelongsToOwner(childId: String, ownerId: String) =
        assertNavActionBelongsToOwner.execute(actionId = childId, ownerId = ownerId)
}
