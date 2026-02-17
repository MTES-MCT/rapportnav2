package fr.gouv.dgampa.rapportnav.domain.repositories.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.springframework.data.domain.Page
import java.time.Instant
import java.util.UUID

interface IApiKeyAuditRepository {
    fun findAllPaginated(page: Int, size: Int): Page<ApiKeyAuditModel>
    fun findByApiKeyIdAndTimestampAfter(apiKeyId: UUID, after: Instant): List<ApiKeyAuditModel>
    fun findByIpAddressAndTimestampAfter(ip: String, after: Instant): List<ApiKeyAuditModel>
    fun countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(apiKeyId: UUID, after: Instant): Long
    fun save(audit: ApiKeyAuditModel): ApiKeyAuditModel
}
