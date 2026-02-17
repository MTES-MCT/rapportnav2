package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.apikey

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.UUID

interface IDBApiKeyAuditRepository : JpaRepository<ApiKeyAuditModel, Int> {
    fun findAllByOrderByTimestampDesc(pageable: Pageable): Page<ApiKeyAuditModel>
    fun save(audit: ApiKeyAuditModel): ApiKeyAuditModel
    fun findByApiKeyIdAndTimestampAfter(apiKeyId: UUID, after: Instant): List<ApiKeyAuditModel>
    fun findByIpAddressAndTimestampAfter(ip: String, after: Instant): List<ApiKeyAuditModel>
    fun countByApiKeyIdAndSuccessIsTrueAndTimestampAfter(apiKeyId: UUID, after: Instant): Long
}
