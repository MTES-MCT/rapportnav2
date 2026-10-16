package fr.gouv.dgampa.rapportnav.infrastructure.api.security

/**
 * Verifies that a child resource (identified by [CheckMissionAccess.child]) belongs to the owner resolved
 * from [CheckMissionAccess.param]. One implementation per [MissionChildResource]; [MissionAccessAspect]
 * selects the matching guard by its [resource].
 *
 * Implementations live in infrastructure and delegate the actual decision to a domain use case, so the
 * aspect stays logic-free and a denial surfaces as the same [fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException]
 * (403) a manual call would throw.
 */
interface ChildAccessGuard {
    val resource: MissionChildResource

    /**
     * @throws fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException when the child identified
     * by [childId] exists but does not belong to [ownerId]. A child that does not exist yet is allowed.
     */
    fun assertBelongsToOwner(childId: String, ownerId: String)
}
