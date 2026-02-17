package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user

import fr.gouv.dgampa.rapportnav.domain.repositories.user.IAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBAuthenticationAuditRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository

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
}