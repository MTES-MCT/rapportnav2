package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlNavigationRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlNavigationRepository(
    private val dbControlNavigationRulesRepository: IDBControlNavigationRepository,
    private val mapper: ObjectMapper,
) : IControlNavigationRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlNavigation> {
        // TODO call correct function filtering by mission id
        TODO("Not yet implemented")
//        return dbControlNavigationRulesRepository.findAll().map { it.toControlNavigationRules() }

    }

}
