package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationRules
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRulesRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlNavigationRulesRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlNavigationRulesRepository(
    private val dbControlNavigationRulesRepository: IDBControlNavigationRulesRepository,
    private val mapper: ObjectMapper,
) : IControlNavigationRulesRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlNavigationRules> {
        // TODO call correct function filtering by mission id
        TODO("Not yet implemented")
//        return dbControlNavigationRulesRepository.findAll().map { it.toControlNavigationRules() }

    }

}
