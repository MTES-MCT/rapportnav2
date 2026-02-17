package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.apikey.ApiKeyAuditModel
import org.springframework.data.domain.Page

data class PaginatedApiKeyAuditOutput(
    val items: List<ApiKeyAuditOutput>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun fromPage(page: Page<ApiKeyAuditModel>): PaginatedApiKeyAuditOutput {
            return PaginatedApiKeyAuditOutput(
                items = page.content.map { ApiKeyAuditOutput.fromModel(it) },
                page = page.number,
                pageSize = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}