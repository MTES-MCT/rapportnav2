package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.sati

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.SatiListFilter
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModel
import org.springframework.data.jpa.domain.Specification

object SatiSpecifications {

    fun fromFilter(filter: SatiListFilter): Specification<SatiModel> {
        val predicates = mutableListOf<Specification<SatiModel>>()

        filter.module?.let { module ->
            predicates.add(Specification { root, _, cb -> cb.equal(root.get<String>("module"), module.name) })
        }
        filter.status?.let { status ->
            predicates.add(Specification { root, _, cb -> cb.equal(root.get<String>("status"), status.name) })
        }
        filter.actionId?.let { actionId ->
            predicates.add(Specification { root, _, cb -> cb.equal(root.get<String>("actionId"), actionId) })
        }
        filter.search?.let { search ->
            predicates.add(
                Specification { root, _, cb -> cb.like(cb.lower(root.get("actionId")), "%${search.lowercase()}%") }
            )
        }

        return if (predicates.isEmpty()) Specification.unrestricted() else Specification.allOf(predicates)
    }
}
