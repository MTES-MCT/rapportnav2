package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.sati

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.SatiListFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@UseCase
class GetSatiList(
    private val repository: ISatiRepository,
) {
    fun execute(
        page: Int,
        size: Int,
        sort: String,
        module: SatiModuleType?,
        status: SatiStatusType?,
        actionId: String?,
        search: String?
    ): Page<SatiEntity> {
        val (sortField, sortDirection) = sort.split(",").let {
            it[0] to (it.getOrNull(1)?.let { dir -> Sort.Direction.fromString(dir) } ?: Sort.Direction.DESC)
        }
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField))
        val filter = SatiListFilter(module = module, status = status, actionId = actionId, search = search)

        return repository.findAll(pageable, filter)
    }
}
