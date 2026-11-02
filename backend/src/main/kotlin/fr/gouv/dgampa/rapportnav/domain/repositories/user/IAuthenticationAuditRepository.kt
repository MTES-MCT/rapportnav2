package fr.gouv.dgampa.rapportnav.domain.repositories.user

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import org.springframework.data.domain.Page
import java.time.Instant

interface IAuthenticationAuditRepository {
    fun save(audit: AuthenticationAuditModel): AuthenticationAuditModel
    fun findAllPaginated(page: Int, size: Int): Page<AuthenticationAuditModel>
    fun countFailuresByEmailSince(email: String, since: Instant): Long
    fun countFailuresByIpAddressSince(ipAddress: String, since: Instant): Long
}