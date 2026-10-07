package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository

/**
 * Resolves the cloisonnement scope of the authenticated user: the set of service ids (and the union of
 * their control units) the user is allowed to access.
 *
 * The scope is the user's own service plus any linked service, resolved **symmetrically**:
 * - the service its `service_linked_id` points to (forward), and
 * - any service whose `service_linked_id` points back to it (reverse).
 *
 * Fails closed: an unauthenticated user, or a user with no service, gets an empty scope (no access).
 * The user's own service id (carried on the re-validated token) is always in scope — even if its
 * service row can't be loaded (soft-deleted / transient read failure) — so a data-integrity glitch
 * never 403s a user out of their OWN data. Only *linked* services require a readable row (fail closed).
 */
@UseCase
class GetUserServiceScope(
    private val getUserFromToken: GetUserFromToken,
    private val serviceRepo: IServiceRepository,
) {
    data class Scope(val serviceIds: Set<Int>, val controlUnitIds: Set<Int>)

    fun execute(): Scope {
        val serviceId = getUserFromToken.execute()?.serviceId ?: return EMPTY
        val service = serviceRepo.findById(serviceId).orElse(null)

        val services = buildList {
            service?.let(::add)
            service?.serviceLinkedId?.let { linkedId ->
                serviceRepo.findById(linkedId).orElse(null)?.let(::add) // forward link
            }
            addAll(serviceRepo.findByServiceLinkedId(serviceId)) // reverse links (symmetric)
        }.distinctBy { it.id }

        return Scope(
            // The authenticated user's own service id is authoritative and always granted, even when its
            // row is unreadable; linked services only count if their row (and id) actually resolved.
            serviceIds = (services.mapNotNull { it.id } + serviceId).toSet(),
            controlUnitIds = services.flatMap { it.controlUnits ?: emptyList() }.toSet(),
        )
    }

    companion object {
        private val EMPTY = Scope(emptySet(), emptySet())
    }
}
