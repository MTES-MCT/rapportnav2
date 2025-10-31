package fr.gouv.dgampa.rapportnav.domain.repositories.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import java.time.Instant
import java.util.UUID

interface IApiKeyAuditRepository {
    fun findByApiKeyIdAndTimestampAfter(apiKeyId: UUID, after: Instant): List<ApiKeyAuditModel>
//    fun countSuccessfulRequestsSince(apiKeyId: UUID, after: Instant): Long
    fun save(audit: ApiKeyAuditModel): ApiKeyAuditModel
}
