package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.infraction.v1.adapters.output

data class PaginatedOutput<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val totalItems: Long,
    val totalPages: Int
)
