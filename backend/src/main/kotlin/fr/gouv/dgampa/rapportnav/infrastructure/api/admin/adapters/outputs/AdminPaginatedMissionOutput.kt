package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.springframework.data.domain.Page

data class AdminPaginatedMissionOutput(
    val items: List<AdminMissionOutput>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun fromPage(page: Page<MissionModel>): AdminPaginatedMissionOutput {
            return AdminPaginatedMissionOutput(
                items = page.content.map { AdminMissionOutput.fromModel(it) },
                page = page.number,
                pageSize = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}
