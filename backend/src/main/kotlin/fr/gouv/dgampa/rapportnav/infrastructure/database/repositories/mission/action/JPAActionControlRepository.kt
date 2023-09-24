package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionControlRepository
import org.springframework.stereotype.Repository

@Repository
class JPAActionControlRepository (
    private val dbActionModelRepository: IDBActionControlRepository,
    private val mapper: ObjectMapper,
) : INavActionControlRepository {

    override fun findAllByMissionId(missionId: Int): List<NavAction> {
        return dbActionModelRepository.findAllByMissionId(missionId).map { it.toNavAction() }
    }


}
