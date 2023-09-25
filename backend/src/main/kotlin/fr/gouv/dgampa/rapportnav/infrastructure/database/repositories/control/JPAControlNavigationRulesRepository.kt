package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlNavigationRules
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlNavigationRulesRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.control.IDBControlNavigationRulesRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlNavigationRulesRepository(
    private val dbControlNavigationRulesRepository: IDBControlNavigationRulesRepository,
    private val mapper: ObjectMapper,
) : IControlNavigationRulesRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlNavigationRules> {
        // TODO call correct function filtering by mission id
        return dbControlNavigationRulesRepository.findAll().map { it.toControlNavigationRules() }

    }

}
