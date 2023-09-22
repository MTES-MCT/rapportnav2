package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionRepository
import org.springframework.stereotype.Repository

@Repository
class JPAActionRepository (
    private val dbActionRepository: IDBActionRepository,
    private val mapper: ObjectMapper,
) : INavActionRepository {

    override fun findAllByMissionId(missionId: Int): List<NavAction> {
        return dbActionRepository.findAllByMissionId(missionId).map { it.toNavAction() }
    }


}
