package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import org.springframework.data.domain.Page

data class AdminPaginatedAuthenticationAuditOutput(
    val items: List<AdminAuthenticationAuditOutput>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun fromPage(page: Page<AuthenticationAuditModel>): AdminPaginatedAuthenticationAuditOutput {
            return AdminPaginatedAuthenticationAuditOutput(
                items = page.content.map { AdminAuthenticationAuditOutput.fromModel(it) },
                page = page.number,
                pageSize = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}
