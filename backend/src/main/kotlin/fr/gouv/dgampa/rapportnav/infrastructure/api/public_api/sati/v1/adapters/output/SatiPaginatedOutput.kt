package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import org.springframework.data.domain.Page

data class SatiPaginatedOutput(
    val items: List<SatiListOutput>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
) {
    companion object {
        fun fromPage(page: Page<SatiEntity>): SatiPaginatedOutput {
            return SatiPaginatedOutput(
                items = page.content.map { SatiOutputMapper.toListOutput(it) },
                page = page.number,
                pageSize = page.size,
                totalItems = page.totalElements,
                totalPages = page.totalPages
            )
        }
    }
}
