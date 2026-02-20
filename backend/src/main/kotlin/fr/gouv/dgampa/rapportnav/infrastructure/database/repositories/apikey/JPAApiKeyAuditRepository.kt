package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.apikey

import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey.IDBApiKeyAuditRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class JPAApiKeyAuditRepository(
    private val repository: IDBApiKeyAuditRepository,
): IApiKeyAuditRepository {

    override fun findAllPaginated(page: Int, size: Int): Page<ApiKeyAuditModel> {
        return repository.findAllByOrderByTimestampDesc(PageRequest.of(page, size))
    }

    override fun save(audit: ApiKeyAuditModel): ApiKeyAuditModel {
        return repository.save(audit)
    }

    override fun findByApiKeyIdAndTimestampAfter(apiKeyId: UUID, after: Instant): List<ApiKeyAuditModel> {
        return repository.findByApiKeyIdAndTimestampAfter(apiKeyId, after)
    }

    override fun countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(apiKeyId: UUID, after: Instant): Long {
        return repository.countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(apiKeyId, after)
    }

    override fun findByIpAddressAndTimestampAfter(ip: String, after: Instant): List<ApiKeyAuditModel> {
        return repository.findByIpAddressAndTimestampAfter(ip, after)
    }
}
