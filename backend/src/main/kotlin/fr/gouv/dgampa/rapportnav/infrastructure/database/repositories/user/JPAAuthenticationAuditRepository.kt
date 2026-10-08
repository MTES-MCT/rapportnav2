package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user

import fr.gouv.dgampa.rapportnav.domain.repositories.user.IAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBAuthenticationAuditRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
class JPAAuthenticationAuditRepository(
    private val repository: IDBAuthenticationAuditRepository,
) : IAuthenticationAuditRepository {

    override fun save(audit: AuthenticationAuditModel): AuthenticationAuditModel {
        return repository.save(audit)
    }

    override fun findAllPaginated(page: Int, size: Int): Page<AuthenticationAuditModel> {
        return repository.findAllByOrderByTimestampDesc(PageRequest.of(page, size))
    }

    override fun countFailuresByEmailSince(email: String, since: Instant): Long {
        return repository.countByEmailAndSuccessFalseAndTimestampAfter(email, since)
    }

    override fun countFailuresByIpAddressSince(ipAddress: String, since: Instant): Long {
        return repository.countByIpAddressAndSuccessFalseAndTimestampAfter(ipAddress, since)
    }
}