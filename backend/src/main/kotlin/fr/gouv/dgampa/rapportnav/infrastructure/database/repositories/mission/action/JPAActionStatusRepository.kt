package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionStatusRepository
import org.springframework.stereotype.Repository

@Repository
class JPAActionStatusRepository (
    private val dbActionStatusRepository: IDBActionStatusRepository,
    private val mapper: ObjectMapper,
) : INavActionStatusRepository {

//    override fun findOngoingStatusForMission(missionId: Int): ActionStatus {
//        val all =  dbActionStatusRepository.findAll().map { it.toActionStatus() }
//        return all.first()
//    }


}
