package fr.gouv.dgampa.rapportnav.domain.use_cases.apikey

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.springframework.data.domain.Page

@UseCase
class GetApiKeyAuditList(
    private val auditRepository: IApiKeyAuditRepository,
) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }

    fun execute(page: Int = 0, size: Int = DEFAULT_PAGE_SIZE): Page<ApiKeyAuditModel> {
        return auditRepository.findAllPaginated(page, size)
    }
}