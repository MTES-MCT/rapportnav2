package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.springframework.data.domain.Page

data class AdminPaginatedMissionActionOutput(
    val items: List<AdminMissionActionOutput>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun fromPage(page: Page<MissionActionModel>): AdminPaginatedMissionActionOutput {
            return AdminPaginatedMissionActionOutput(
                items = page.content.map { AdminMissionActionOutput.fromModel(it) },
                page = page.number,
                pageSize = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}
