package fr.gouv.dgampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IAuthenticationAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import org.springframework.data.domain.Page

@UseCase
class GetAuthenticationAuditList(
    private val auditRepository: IAuthenticationAuditRepository,
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }

    fun execute(page: Int = 0, size: Int = DEFAULT_PAGE_SIZE): Page<AuthenticationAuditModel> {
        return auditRepository.findAllPaginated(page, size)
    }
}