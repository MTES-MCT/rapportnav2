package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.UUID

interface IDBApiKeyAuditRepository : JpaRepository<ApiKeyAuditModel, Int> {
    fun save(audit: ApiKeyAuditModel): ApiKeyAuditModel
    fun findByApiKeyIdAndTimestampAfter(apiKeyId: UUID, after: Instant): List<ApiKeyAuditModel>
//    fun countSuccessfulRequestsSince(apiKeyId: UUID, after: Instant): Long
}
