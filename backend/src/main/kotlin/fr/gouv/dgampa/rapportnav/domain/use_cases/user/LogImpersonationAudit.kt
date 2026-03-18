package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.ImpersonationAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBImpersonationAuditRepository
import org.slf4j.LoggerFactory
import java.time.Instant

/**
 * Logs impersonation events for audit purposes.
 * Throws exception on failure to ensure fail-closed behavior.
 */
@UseCase
class LogImpersonationAudit(
    private val repository: IDBImpersonationAuditRepository
) {
    private val logger = LoggerFactory.getLogger(LogImpersonationAudit::class.java)

    /**
     * Logs an impersonation audit entry.
     * @throws BackendInternalException if the audit entry cannot be saved
     */
    fun execute(
        adminUserId: Int,
        targetServiceId: Int,
        ipAddress: String?
    ) {
        try {
            val audit = ImpersonationAuditModel(
                adminUserId = adminUserId,
                targetServiceId = targetServiceId,
                ipAddress = ipAddress,
                timestamp = Instant.now()
            )
            repository.save(audit)
        } catch (e: Exception) {
            logger.error("Failed to log impersonation audit: ${e.message}", e)
            throw BackendInternalException(
                code = BackendInternalErrorCode.AUDIT_LOGGING_FAILED,
                message = "Cannot proceed with impersonation: audit logging failed",
                originalException = e
            )
        }
    }
}
