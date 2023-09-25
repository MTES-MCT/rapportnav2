package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.control.IDBControlGensDeMerRepository
import org.springframework.stereotype.Repository


@Repository
class JPAControlGensDeMerRepository(
    private val dbControlGensDeMerRepository: IDBControlGensDeMerRepository,
    private val mapper: ObjectMapper,
) : IControlGensDeMerRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlGensDeMer> {
        // TODO call correct function filtering by mission id
        return dbControlGensDeMerRepository.findAll().map { it.toControlGensDeMer() }

    }

}
