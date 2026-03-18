package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById

/**
 * Validates whether a user can impersonate a specific service.
 * Only ADMIN users can impersonate, and the target service must exist and not be deleted.
 */
@UseCase
class ValidateImpersonation(
    private val getServiceById: GetServiceById
) {
    /**
     * Validates that the user can impersonate the target service.
     *
     * @param user The user attempting to impersonate
     * @param targetServiceId The service ID to impersonate
     * @return The ServiceEntity if validation passes
     * @throws IllegalArgumentException if user is not an admin
     * @throws IllegalArgumentException if service does not exist or is deleted
     */
    fun execute(user: User, targetServiceId: Int): ServiceEntity {
        // Check user has ADMIN role
        if (!user.roles.contains(RoleTypeEnum.ADMIN)) {
            throw IllegalArgumentException("Only ADMIN users can impersonate services")
        }

        // Check target service exists
        val service = getServiceById.execute(targetServiceId)
            ?: throw IllegalArgumentException("Service with id $targetServiceId does not exist")

        // Check service is not deleted
        if (service.deletedAt != null) {
            throw IllegalArgumentException("Service with id $targetServiceId has been deleted")
        }

        return service
    }
}
