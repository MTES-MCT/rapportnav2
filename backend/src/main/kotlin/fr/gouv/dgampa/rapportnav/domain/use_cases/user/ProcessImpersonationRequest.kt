package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.user.ImpersonationContext
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.ImpersonationContextHolder
import org.slf4j.LoggerFactory

/**
 * Use case for processing impersonation requests.
 * Validates the request, sets the impersonation context, and logs the audit entry.
 */
@UseCase
class ProcessImpersonationRequest(
    private val validateImpersonation: ValidateImpersonation,
    private val logImpersonationAudit: LogImpersonationAudit,
    private val impersonationContextHolder: ImpersonationContextHolder
) {
    private val logger = LoggerFactory.getLogger(ProcessImpersonationRequest::class.java)

    /**
     * Process an impersonation request.
     *
     * @param user The authenticated user making the request
     * @param targetServiceId The service ID to impersonate (null if not impersonating)
     * @param ipAddress The client IP address for audit logging
     * @return The user, potentially with modified serviceId and reduced roles if impersonation is active
     * @throws BackendForbiddenException if non-admin attempts to impersonate
     */
    fun execute(user: User, targetServiceId: Int?, ipAddress: String?): User {
        if (targetServiceId == null) {
            return user
        }

        // Only admins can impersonate - throw 403 for unauthorized attempts
        if (!user.roles.contains(RoleTypeEnum.ADMIN)) {
            logger.warn("Non-admin user ${user.id} attempted to impersonate service $targetServiceId")
            throw BackendForbiddenException(
                code = BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION,
                message = "Impersonation requires ADMIN role"
            )
        }

        return try {
            val service = validateImpersonation.execute(user, targetServiceId)

            // Audit log FIRST - fail-closed: if we can't audit, we can't impersonate
            logImpersonationAudit.execute(
                adminUserId = user.id ?: 0,
                targetServiceId = targetServiceId,
                ipAddress = ipAddress
            )

            // Set request-scoped context (available throughout request lifecycle)
            impersonationContextHolder.set(
                ImpersonationContext(
                    isActive = true,
                    originalServiceId = user.serviceId,
                    impersonatedServiceId = targetServiceId,
                    impersonatedServiceName = service.name
                )
            )

            logger.info("Admin user ${user.id} impersonating service ${service.name} (id: $targetServiceId)")

            // Return user with modified serviceId and ROLE_ADMIN removed
            // This prevents accessing admin endpoints while impersonating
            val impersonatedRoles = user.roles.filter { it != RoleTypeEnum.ADMIN }

            user.copy(
                serviceId = targetServiceId,
                roles = impersonatedRoles
            )
        } catch (e: BackendInternalException) {
            logger.error("Impersonation denied - audit logging failed for user ${user.id}: ${e.message}")
            throw BackendForbiddenException(
                code = BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION,
                message = "Impersonation unavailable: audit system error"
            )
        } catch (e: IllegalArgumentException) {
            logger.warn("Impersonation failed for user ${user.id}: ${e.message}")
            throw BackendForbiddenException(
                code = BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION,
                message = "Invalid impersonation target"
            )
        }
    }
}
