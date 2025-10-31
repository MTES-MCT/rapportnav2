package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.apikey

import fr.gouv.dgampa.rapportnav.domain.repositories.apikey.IApiKeyAuditRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey.IDBApiKeyAuditRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class JPAApiKeyAuditRepository(
    private val repository: IDBApiKeyAuditRepository,
): IApiKeyAuditRepository {

    override fun save(audit: ApiKeyAuditModel): ApiKeyAuditModel {
        return repository.save(audit)
    }

    override fun findByApiKeyIdAndTimestampAfter(apiKeyId: UUID, after: Instant): List<ApiKeyAuditModel> {
        return repository.findByApiKeyIdAndTimestampAfter(apiKeyId, after)
    }

//    override fun countSuccessfulRequestsSince(apiKeyId: UUID, after: Instant): Long {
//        return 1L
//    }
}
