package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import org.springframework.data.domain.Page

data class PaginatedAuthenticationAuditOutput(
    val items: List<AuthenticationAuditOutput>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun fromPage(page: Page<AuthenticationAuditModel>): PaginatedAuthenticationAuditOutput {
            return PaginatedAuthenticationAuditOutput(
                items = page.content.map { AuthenticationAuditOutput.fromModel(it) },
                page = page.number,
                pageSize = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}