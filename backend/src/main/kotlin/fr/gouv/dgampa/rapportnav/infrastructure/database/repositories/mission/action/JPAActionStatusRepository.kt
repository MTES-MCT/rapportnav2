package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionStatusRepository
import org.springframework.stereotype.Repository

@Repository
class JPAActionStatusRepository (
    private val dbActionStatusRepository: IDBActionStatusRepository,
    private val mapper: ObjectMapper,
) : INavActionStatusRepository {

    override fun findAllByMissionId(missionId: Int): List<ActionStatus> {
        // TODO call correct function filtering by mission id
        return dbActionStatusRepository.findAllById(listOf(missionId)).map { it.toActionStatus() }
    }


}
