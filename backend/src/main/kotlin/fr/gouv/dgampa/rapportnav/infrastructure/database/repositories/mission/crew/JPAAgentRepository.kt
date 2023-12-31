package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRepository
import org.springframework.stereotype.Repository

@Repository
class JPAAgentRepository(
    private val dbAgentRepository: IDBAgentRepository,
    private val mapper: ObjectMapper
) : IAgentRepository {

    override fun findAll(): List<AgentModel> {
        return dbAgentRepository.findAll()
    }

    override fun findByServiceId(serviceId: Int): List<AgentModel> {
        return dbAgentRepository.findByServicesId(serviceId)
    }
}
