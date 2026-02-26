package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.domain.Page

data class AdminPaginatedGeneralInfosOutput(
    val items: List<AdminGeneralInfosOutput>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun fromPage(page: Page<MissionGeneralInfoModel>): AdminPaginatedGeneralInfosOutput {
            return AdminPaginatedGeneralInfosOutput(
                items = page.content.map { AdminGeneralInfosOutput.fromModel(it) },
                page = page.number,
                pageSize = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}
